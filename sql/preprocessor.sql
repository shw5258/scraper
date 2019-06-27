
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
use scraped;
select prod_id from product where soldout = true;
select prod_id from product where main_im is not null limit 20 offset 100;

#필요없는 상품 제거
DELETE FROM product WHERE SOLDOUT IS TRUE OR variants IS TRUE OR must_call IS TRUE OR another_form IS TRUE;

#전체상품개수
SELECT COUNT(*) FROM product where changed = true;

#신상품 업로드 되면 변경표시 초기화(가격변동 기록 위해)
update product set changed = false where changed = true;

#코스트코에 없는 상품번호 삭제
#delete from product where prod_id in ();

#특정 상품 제거
delete from product where prod_id = 617793;

#품절된 상품을 품절처리하기위해 상품코드출력
select prod_id from product where soldout = true;
desc product;
#잘못 수집된 네이버카테고리 수정 changed true
update product set cat_naver = 50000458, changed = true where prod_id = 609556;

#나머지 요청
show databases;
use scraped;
show tables;
select * from category;

select CHAR_LENGTH(name) from product limit 10;
