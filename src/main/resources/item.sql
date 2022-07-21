create table items
(
    id       serial
        constraint items_pk
            primary key,
    name     varchar not null,
    path     varchar not null,
    username varchar not null,
    token    varchar
);

alter table items
    owner to postgres;

create unique index items_id_uindex
    on items (id);

