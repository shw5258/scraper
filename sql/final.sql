use scraped;
#맨처음 과거 품절된 상품이 재입고 되었는지를 알아보기 위해 우선 모든 상품이 품절이 아닌 것으로 상정한다
update product set soldout = false where soldout = true;
#이번 정보수집에서 변화된 것만 나타내도록 수집전에 모든상품을 변경전으로 상정한다
update product set changed = false where changed = true;
#특정제품을 추가해야할 경우
update product set changed = true where prod_id = 611353;
#판매중지인 상품중에 품절이 아닌 상품을 찾아내어 판매중으로 바꿔준다
select prod_id from product where soldout = false and prod_id in (623546,
                                                                  623545,
                                                                  623544,
                                                                  623542,
                                                                  617277,
                                                                  517508,
                                                                  1900728,
                                                                  1193792,
                                                                  709341,
                                                                  619702,
                                                                  619699,
                                                                  619298,
                                                                  617422,
                                                                  616402,
                                                                  614435,
                                                                  612039,
                                                                  609910,
                                                                  604239,
                                                                  604229,
                                                                  601435,
                                                                  585815,
                                                                  585814,
                                                                  526125,
                                                                  523636,
                                                                  519435,
                                                                  519429,
                                                                  518605,
                                                                  518584,
                                                                  517505,
                                                                  513154,
                                                                  502150,
                                                                  502149,
                                                                  502148,
                                                                  11915,
                                                                  623632,
                                                                  609967,
                                                                  1500105,
                                                                  1280714,
                                                                  1209605,
                                                                  1031593,
                                                                  922274,
                                                                  709341,
                                                                  3333333,
                                                                  2000581,
                                                                  1900728,
                                                                  1900726,
                                                                  1900711,
                                                                  1500180,
                                                                  1284674,
                                                                  1183296,
                                                                  1055999,
                                                                  1043039,
                                                                  1040023,
                                                                  1002373,
                                                                  998885,
                                                                  995551,
                                                                  901991,
                                                                  862724,
                                                                  781893,
                                                                  747267,
                                                                  623114,
                                                                  623094,
                                                                  622689,
                                                                  621748,
                                                                  621406,
                                                                  620964,
                                                                  620054,
                                                                  619806,
                                                                  619533,
                                                                  619497,
                                                                  619455,
                                                                  618590,
                                                                  617782,
                                                                  617771,
                                                                  617212,
                                                                  616415,
                                                                  616255,
                                                                  615896,
                                                                  615824,
                                                                  614803,
                                                                  613421,
                                                                  613000,
                                                                  612933,
                                                                  611700,
                                                                  611363,
                                                                  611188,
                                                                  610739,
                                                                  610737,
                                                                  608235,
                                                                  608097,
                                                                  608062,
                                                                  607642,
                                                                  607569,
                                                                  606491,
                                                                  605796,
                                                                  605622,
                                                                  605614,
                                                                  605484,
                                                                  605064,
                                                                  603151,
                                                                  600959,
                                                                  600473,
                                                                  600471,
                                                                  599821,
                                                                  597451,
                                                                  566540,
                                                                  526595,
                                                                  526177,
                                                                  524341,
                                                                  524114,
                                                                  523780,
                                                                  522089,
                                                                  515855,
                                                                  515852,
                                                                  512877,
                                                                  511438,
                                                                  510340,
                                                                  509856,
                                                                  509694,
                                                                  509337,
                                                                  509314,
                                                                  509129,
                                                                  509000,
                                                                  508910,
                                                                  507897,
                                                                  507200,
                                                                  507122,
                                                                  470974,
                                                                  346888,
                                                                  137378,
                                                                  507897,
                                                                  507200,
                                                                  507122,
                                                                  470974,
                                                                  346888,
                                                                  137378,
                                                                  614805,
                                                                  519727);
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
#그다음에 manager로 품절상품, 삭제상품, 가격변동상품을 걸러낸다. 이부분을 프로그램으로 한번에 처리하자
#상품이 있는데 등록되지 않은 상품번호
#611353