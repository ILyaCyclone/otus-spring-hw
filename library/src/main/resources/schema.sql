CREATE TABLE author (
  author_id INT PRIMARY KEY,
  firstname varchar NOT NULL,
  lastname varchar NOT NULL,
  homeland varchar
);



CREATE TABLE genre (
  genre_id INT PRIMARY KEY,
  name varchar NOT NULL
);



CREATE TABLE book (
  book_id INT PRIMARY KEY,

  author_id INT NOT NULL REFERENCES author(author_id),
  genre_id INT REFERENCES genre(genre_id),

  title varchar NOT NULL,
  year int
);