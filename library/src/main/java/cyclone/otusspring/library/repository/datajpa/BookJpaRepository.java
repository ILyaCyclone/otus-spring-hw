package cyclone.otusspring.library.repository.datajpa;

import cyclone.otusspring.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByOrderByTitle();

    List<Book> findByTitleContainingIgnoreCaseOrderByTitle(String title);
}
