package cyclone.otusspring.library.repository;


import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.mongo.MongoBookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final MongoBookRepository mongoRepository;

    public BookRepositoryImpl(MongoBookRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public List<Book> findAll() {
        return mongoRepository.findAllByOrderByTitle();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return mongoRepository.findByTitleContainingIgnoreCaseOrderByTitle(title);
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        return mongoRepository.findByAuthorOrderByTitle(author);
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        return mongoRepository.findByGenreOrderByTitle(genre);
    }

    @Override
    public Book findOne(String id) {
        return mongoRepository.findById(id).orElseThrow(() -> new NotFoundException("Book ID " + id + " not found"));
    }

    @Override
    public Book save(Book book) {
        return mongoRepository.save(book);
    }

    @Override
    public void delete(String id) {
        if (!mongoRepository.existsById(id)) throw new NotFoundException("Book ID " + id + " not found");
        mongoRepository.deleteById(id);
    }

    @Override
    public void delete(Book book) {
        mongoRepository.delete(book);
    }

    @Override
    public boolean exists(String id) {
        return mongoRepository.existsById(id);
    }
}
