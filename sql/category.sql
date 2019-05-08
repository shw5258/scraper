create table scraped.category
(
    cat_large  tinyint unsigned not null,
    cat_medium tinyint unsigned not null,
    cat_small  tinyint unsigned not null,
    name       varchar(25)      null,
    primary key (cat_large, cat_medium, cat_small)
)
    charset = utf8;


