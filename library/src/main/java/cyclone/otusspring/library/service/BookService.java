package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.BookDetails;
import cyclone.otusspring.library.model.Book;

import java.util.List;

public interface BookService {
    Book createBook(String title, Integer year, long authorId, long genreId);

    List<Book> findAll();

    List<BookDetails> findAllWithDetails();
}
