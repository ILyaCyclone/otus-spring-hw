CREATE TABLE IF NOT EXISTS author (
    id varchar(24) primary key,
    firstname varchar(100) not null,
    lastname varchar(100) not null,
    homeland varchar(100)
);



CREATE TABLE IF NOT EXISTS genre (
  id varchar(24) primary key,
  name varchar(100) NOT NULL
);



CREATE TABLE IF NOT EXISTS book (
  id varchar(24) primary key,

  author_id varchar(24) NOT NULL REFERENCES author(id),
  genre_id varchar(24) REFERENCES genre(id),

  title varchar(200) NOT NULL,
  year int
);



CREATE TABLE IF NOT EXISTS comment (
  id bigserial primary key,
  commentator varchar(50),
  text text,
  date timestamp,

  book_id varchar(24) NOT NULL REFERENCES book(id)
);