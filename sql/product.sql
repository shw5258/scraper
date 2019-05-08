create table scraped.product
(
    prod_id      int                       not null
        primary key,
    name         varchar(100) charset utf8 null,
    cat_large    tinyint unsigned          not null,
    cat_medium   tinyint unsigned          not null,
    cat_small    tinyint unsigned          not null,
    soldout      tinyint(1) default 0      not null,
    variants     tinyint(1) default 0      not null,
    price        int                       null,
    detail       text                      null,
    cat_naver    int                       null,
    must_call    tinyint(1) default 0      null,
    present      tinyint(1) default 1      null,
    another_form tinyint(1) default 0      not null,
    main_im      varchar(20)               null,
    comple_im    varchar(255)              null
);


