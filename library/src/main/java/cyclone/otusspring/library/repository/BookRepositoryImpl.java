package cyclone.otusspring.library.repository;


import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.repository.mongo.MongoBookRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class BookRepositoryImpl implements BookRepository {

    private final MongoBookRepository jpaRepository;

    public BookRepositoryImpl(MongoBookRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Book> findAll() {
        return jpaRepository.findAllByOrderByTitle();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return jpaRepository.findByTitleContainingIgnoreCaseOrderByTitle(title);
    }

    @Override
    public Book findOne(String id) {
        return jpaRepository.findById(id).orElseThrow(() -> new NotFoundException("Book ID " + id + " not found"));
    }

    @Override
    @Transactional
    public Book save(Book book) {
        return jpaRepository.save(book);
    }

    @Override
    @Transactional
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Book book) {
        jpaRepository.delete(book);
    }

    @Override
    public boolean exists(String id) {
        return jpaRepository.existsById(id);
    }
}
