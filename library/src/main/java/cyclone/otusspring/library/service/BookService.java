package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.model.Book;

import java.util.List;

public interface BookService {
    Book createBook(BookDto bookDto);

    List<Book> findAll();
}