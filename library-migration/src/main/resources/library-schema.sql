CREATE TABLE IF NOT EXISTS author (
    id bigserial primary key,
    firstname varchar(100) not null,
    lastname varchar(100) not null,
    homeland varchar(100)
);



CREATE TABLE IF NOT EXISTS genre (
  id bigserial primary key,
  name varchar(100) NOT NULL
);



CREATE TABLE IF NOT EXISTS book (
  id bigserial primary key,

  author_id BIGINT NOT NULL REFERENCES author(id),
  genre_id BIGINT REFERENCES genre(id),

  title varchar(200) NOT NULL,
  year int
);