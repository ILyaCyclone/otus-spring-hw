package cyclone.otusspring.library.repository.datajpa;

import cyclone.otusspring.library.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genre"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findAllByOrderByTitle();

    @EntityGraph(attributePaths = {"author", "genre"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findByTitleContainingIgnoreCaseOrderByTitle(String title);
}
