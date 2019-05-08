package cyclone.otusspring.library.repository;


import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.repository.datajpa.BookJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class BookRepositoryImpl implements BookRepository {

    private final BookJpaRepository jpaRepository;

    public BookRepositoryImpl(BookJpaRepository jpaRepository) {
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
    public Book findOne(long id) {
        return jpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book ID " + id + " not found"));
    }

    @Override
    @Transactional
    public Book save(Book book) {
        return jpaRepository.save(book);
    }

    @Override
    @Transactional
    public void delete(long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Book book) {
        jpaRepository.delete(book);
    }

    @Override
    public boolean exists(long id) {
        return jpaRepository.existsById(id);
    }
}
