CREATE TABLE author (
    author_id INT PRIMARY KEY AUTO_INCREMENT,
    firstname varchar NOT NULL,
    lastname  varchar NOT NULL,
    homeland  varchar,
    CONSTRAINT author_unique UNIQUE (firstname, lastname, homeland)
);



CREATE TABLE genre (
    genre_id INT PRIMARY KEY AUTO_INCREMENT,
    name     varchar NOT NULL,
    CONSTRAINT genre_unique UNIQUE (name)
);



CREATE TABLE book (
                      book_id   INT PRIMARY KEY AUTO_INCREMENT,
                      author_id INT     NOT NULL REFERENCES author (author_id) ON DELETE CASCADE,
                      genre_id  INT REFERENCES genre (genre_id) ON DELETE CASCADE,

                      title     varchar NOT NULL,
                      year      int,
                      CONSTRAINT book_unique UNIQUE (author_id, title, year)
);