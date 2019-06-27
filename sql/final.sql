#맨처음 과거 품절된 상품이 재입고 되었는지를 알아보기 위해 우선 모든 상품이 품절이 아닌 것으로 상정한다
update product set soldout = false where soldout = true;
#이번 정보수집에서 변화된 것만 나타내도록 수집전에 모든상품을 변경전으로 상정한다
update product set changed = false where changed = true;

#네이버카테고리 수정
desc product;
update product set cat_naver = 50004433 where prod_id = 526125;
commit;
update product set changed = false;
update product set changed = true where prod_id in (7575757,
621642,
620998,
526125);

#특정제품을 추가해야할 경우
update product set changed = true where prod_id in (613421);
select soldout from product where prod_id in (611363);

#판매중지인 상품중에 품절이 아닌 상품을 찾아내어 판매중으로 바꿔준다
select prod_id from product where soldout = false
                              and must_call = false
                              and present = true
                              and another_form = false
                              and variants = false
                              and prod_id in (862931,
614432,
508283,
618296,
600501,
581975,
1282568,
995554,
620604,
619337,
616914,
616785,
604707,
604252,
603051,
623537,
518605,
942083,
620907,
615003,
615002,
615001,
613244,
609967,
609539,
605963,
604297,
1209605,
524125,
1900726,
1600050,
1500180,
1500120,
1140480,
1055999,
1048251,
1043039,
999973,
998885,
901991,
787134,
623094,
623078,
622154,
621749,
621406,
621258,
621257,
620722,
620133,
620063,
619512,
619497,
618828,
618721,
618332,
618206,
617782,
617744,
617449,
616255,
613656,
613243,
612948,
612577,
612429,
612038,
612028,
611880,
611665,
611465,
611462,
611363,
610811,
609799,
609702,
609224,
609018,
607642,
607569,
606878,
606840,
606735,
606491,
605951,
605682,
605622,
605614,
605486,
605484,
605194,
605064,
604492,
604295,
603958,
603625,
602626,
602167,
600959,
599821,
597451,
529919,
529915,
529875,
527060,
526595,
526420,
526177,
524341,
524114,
523780,
523690,
522089,
521270,
516503,
513125,
512877,
511120,
510340,
509856,
509853,
509728,
509727,
509690,
509337,
509314,
509187,
509000,
508755,
508116,
507122,
417210,
381077,
355246,
274022,
519727);

#코스트코에서 검색 안되는 상품 제거(언제 나타날 기약이 없으므로)
delete from product where prod_id in (508142,
508506,
509254,
509807,
511609,
603670,
604130,
607759,
614312,
615446,
620238,
622833);

commit;
#품절된 상품번호 추출 (코스트코에 없는 상품 제거후 실행)
select prod_id from product where soldout is true;
select prod_id from product where variants is true;
select prod_id from product where variants is true and soldout is false and another_form is false;
#빈번한 금지어 처리
show databases;
use scraped;
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
UPDATE product SET detail = REPLACE(detail, '보조기구', '[구기조보]');
UPDATE product SET detail = REPLACE(detail, '최저', '[저최]');
UPDATE product SET detail = REPLACE(detail, '이미테이션', 'imitation');
UPDATE product SET detail = REPLACE(detail, '없음', '없습니다');
UPDATE product SET detail = REPLACE(detail, '코스트코 코리아', '스퀘어팩토리');
UPDATE product SET detail = REPLACE(detail, '코스트코코리아', '스퀘어팩토리');
UPDATE product SET detail = REPLACE(detail, '씨부쓰', 'C.Booth');
UPDATE product SET detail = REPLACE(detail, '*', 'x');
UPDATE product SET name = REPLACE(name, '*', 'x');

#그다음에 manager로 품절상품, 삭제상품, 가격변동상품을 걸러낸다. 이부분을 프로그램으로 한번에 처리하자
#상품이 있는데 등록되지 않은 상품번호
select * from product where changed = true;


select prod_id from product where changed is true and soldout is false and variants is false and must_call is false;
show databases;
use scraped;
#img tag를 일괄수정한다
select prod_id, detail from product where detail like '%<img%';
update product set changed = false;
update product set changed = true where prod_id in (509970,
7575757);

commit;
desc product;

ALTER TABLE product RENAME COLUMN color TO radio_key;
ALTER TABLE product RENAME COLUMN radio TO radio_val;
show databases;
use scraped;
show tables;
delete from product where prod_id = 520023;
commit;
select prod_id, name, radio_key, radio_val, main_im, comple_im from product where variants = true and another_form = false and changed = true;
desc product;

ALTER TABLE product
	CHANGE COLUMN radio_key radio_key varchar(200) NOT NULL DEFAULT '',
	CHANGE COLUMN radio_val radio_val varchar(200) NOT NULL DEFAULT '';

UPDATE product SET radio_key = '', radio_val = '' where variants is true and name is null;
select * from product where radio_key is null;

select MAX(length) from (select CHAR_LENGTH(name) as length from product limit 10) as prod;
select CHAR_LENGTH(name) from product limit 10;
select MAX(CHAR_LENGTH(name)) from product limit 10;

CREATE TABLE variant (
  prod_id varchar(20) NOT NULL,
  name varchar(80),
  cat_large tinyint(3) unsigned not null,
  cat_medium tinyint(3) unsigned not null,
  cat_small tinyint(3) unsigned not null,
  soldout tinyint(1) not null default 0,
  radio_key varchar(10) not null default '',
  radio_val varchar(100) not null default '',
  main_im varchar(20),
  comple_im varchar(255),
  PRIMARY KEY (prod_id)
);

desc product;