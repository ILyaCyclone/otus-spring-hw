create table if not exists author (
    id bigserial primary key,
    firstname varchar(100) not null,
    lastname varchar(100) not null,
    homeland varchar(100)
);