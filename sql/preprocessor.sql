#빈번한 금지어 처리
UPDATE product SET detail = REPLACE(detail, '질병치료', '[료치병질]');
UPDATE product SET detail = REPLACE(detail, '치료', '[료치]');
UPDATE product SET detail = REPLACE(detail, '질병', '[병질]');
UPDATE product SET detail = REPLACE(detail, '의약품', '[품약의]');
UPDATE product SET detail = REPLACE(detail, '예방', '[방예]');
UPDATE product SET detail = REPLACE(detail, '담즙', '[즙담]');
UPDATE product SET detail = REPLACE(detail, '물뽕', '[뽕물]');
UPDATE product SET detail = REPLACE(detail, '직거래', '[래거직]');
UPDATE product SET detail = REPLACE(detail, '멜라토닌', '[닌토라멜]');
UPDATE product SET detail = REPLACE(detail, '약효', '[효약]');
UPDATE product SET detail = REPLACE(detail, '디톡스워터', '[터워스톡디]');
UPDATE product SET detail = REPLACE(detail, '안약', '[약안]');
UPDATE product SET detail = REPLACE(detail, '변비', '[비변]');
UPDATE product SET detail = REPLACE(detail, '고혈압', '[압혈고]');
UPDATE product SET detail = REPLACE(detail, '소독', '[독소]');
UPDATE product SET detail = REPLACE(detail, '도촬', '[촬도]');
UPDATE product SET detail = REPLACE(detail, '도 촬', '도로 촬');
UPDATE product SET detail = REPLACE(detail, 'divx', '');
UPDATE product SET detail = REPLACE(detail, '음독', '[독음]');
UPDATE product SET detail = REPLACE(detail, '가습기살균제', '[제균살기습가]');
UPDATE product SET detail = REPLACE(detail, '질건조', '[조건질]');
UPDATE product SET detail = REPLACE(detail, '음부', '[부음]');
UPDATE product SET detail = REPLACE(detail, '살균', '[균살]');
UPDATE product SET detail = REPLACE(detail, '등황', '[황등]');
UPDATE product SET detail = REPLACE(detail, '[효약]', '[효과]');
UPDATE product SET detail = REPLACE(detail, '물뽕', '[뽕물]');
UPDATE product SET detail = REPLACE(detail, '피부 재생', '[생재 부피]');
UPDATE product SET detail = REPLACE(detail, '재생', '[생재]');
UPDATE product SET detail = REPLACE(detail, '혈액순환', '[환순액혈]');
UPDATE product SET detail = REPLACE(detail, '스테미너', '[너미테스]');
UPDATE product SET detail = REPLACE(detail, '로스분', '[분스로]');
UPDATE product SET detail = REPLACE(detail, '물, 뽕', '물포함, 뽕');
UPDATE product SET detail = REPLACE(detail, '두통', '[통두]');
UPDATE product SET detail = REPLACE(detail, '최저', '[저최]');
UPDATE product SET detail = REPLACE(detail, '이미테이션', 'imitation');
UPDATE product SET detail = REPLACE(detail, '없음', '없습니다');
UPDATE product SET detail = REPLACE(detail, '코스트코 코리아', '스퀘어팩토리');
UPDATE product SET detail = REPLACE(detail, '씨부쓰', 'C.Booth');
UPDATE product SET detail = REPLACE(detail, '*', 'x');
UPDATE product SET name = REPLACE(name, '*', 'x');

update product set cat_naver = 50003257 where prod_id = 2000281;
select name, detail from product where prod_id = 2000281;

#중복된 기록이 있는지 점검
SELECT prod_id, COUNT(prod_id)
FROM product
GROUP BY prod_id
HAVING COUNT(prod_id) > 1;

#중복된 기록을 제거
CREATE TABLE temp LIKE product;
INSERT INTO temp
SELECT * FROM product
GROUP BY prod_id;
DROP TABLE product;
RENAME TABLE temp TO product;

#품절된 상품을 제거하기 위하여 상품번호 추출하기(불필요)
select prod_id from product where soldout = true;
select prod_id from product where soldout is false and variants is false and must_call is false limit 20 offset 100;

#필요없는 상품 제거
DELETE FROM product WHERE SOLDOUT IS TRUE OR variants IS TRUE OR must_call IS TRUE OR another_form IS TRUE;

#전체상품개수
SELECT COUNT(*) FROM product;
select count(*) from product where soldout is true;

#신상품 업로드 되면 변경표시 초기화(가격변동 기록 위해)
update product set changed = false where changed is true;

#코스트코에 없는 상품번호 삭제
delete from product where prod_id in (1050306,
    620718,
614907,
525115);

#특정 상품 제거
delete from product where prod_id = 617793;

#품절된 상품을 품절처리하기위해 상품코드출력
select prod_id from product where soldout = true;

#품절된 상품이 다시 입고되었는지 확인하기 위해 모두 품절이 아닌 상태로 바꾸면 Maneger가 재고정보를 업데이트한다
update product set soldout = false where soldout = true;
update product set changed = false where changed = true;
select max(prod_id) from product where changed = true;
select count(*) from product where prod_id > 707373;
update product set changed = true where prod_id = 623758;

#특정 카테고리의 제품을 일괄추출
select count(*) from product where cat_large = 10 and cat_medium = 10 and cat_small = 2;
select detail from product where changed = true;
update product set changed = true where cat_large = 10 and cat_medium = 10 and cat_small = 2;

#나머지 요청

delete from product where prod_id = 616414;
select count(*) from product where changed = true;
select count(*) from product where main_im is not null;
select detail from product where name = '타이탄 울트라 백팩 쿨러';
select name,detail from product where detail like '%지붕</li>%';
select name, price, soldout, cat_naver, changed from product where prod_id = 622869;
select name, price from product where changed is true;
delete from product where prod_id = 616802 limit 1;
select name, prod_id, soldout, changed from product where prod_id = 616414;
update product set present = true where present is false;
select count(prod_id) from product where changed is true;
select price, cat_large, cat_medium, cat_small, prod_id, changed from product where name like '%PCF-SC15T%';
select prod_id from product where name is null and soldout is false and variants is false and another_form is false and present is true and must_call is false;
