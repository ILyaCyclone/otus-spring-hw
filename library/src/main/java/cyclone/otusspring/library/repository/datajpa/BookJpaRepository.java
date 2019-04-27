package cyclone.otusspring.library.repository.datajpa;

import cyclone.otusspring.library.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static cyclone.otusspring.library.model.Book.GRAPH_WITH_AUTHOR_GENRE;
import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;


public interface BookJpaRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = GRAPH_WITH_AUTHOR_GENRE, type = LOAD)
    List<Book> findAllByOrderByTitle();

    @EntityGraph(value = GRAPH_WITH_AUTHOR_GENRE, type = LOAD)
    List<Book> findByTitleContainingIgnoreCaseOrderByTitle(String title);
}
