create table person
(
    id            serial
        constraint person_pk
            primary key,
    username      varchar not null,
    password      varchar not null,
    year_of_birth integer not null,
    role          varchar not null,
    timer         bigint not null

);

alter table person
    owner to postgres;

create unique index person_id_uindex
    on person (id);

