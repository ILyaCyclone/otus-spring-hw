insert into author (author_id, firstname, lastname, homeland) values
(1, 'Test Arthur', 'Hailey', 'Canada'),
(2, 'Test Isaac', 'Asimov', 'Russia'),
(3, 'Test Gabriel', 'Marquez', 'Argentina');



insert into genre(genre_id, name) values
(1, 'Adventures'),
(2, 'Science fiction'),
(3, 'Novel'),
(4, 'Magic realism');



insert into book (book_id, author_id, genre_id, title, year) values
(1, 1, 1, 'Test Wheels', 1971),
(2, 1, 1, 'Test Airport', 1968),
(3, 2, 2, 'Test The End of Eternity', 1955),
(4, 2, 2, 'Test Foundation', 1951),
(5, 3, 3, 'Test 100 Years of Solitude', 1967);