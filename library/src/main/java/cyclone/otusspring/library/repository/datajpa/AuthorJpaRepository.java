package cyclone.otusspring.library.repository.datajpa;

import cyclone.otusspring.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

    @Query("select a from Author a " +
            "where lower(a.firstname) like concat('%', concat(lower(:name), '%')) " +
            "or lower(a.lastname) like concat('%', concat(lower(:name), '%')) " +
            "order by a.firstname, a.lastname")
    List<Author> findByName(@Param("name") String name);
}
