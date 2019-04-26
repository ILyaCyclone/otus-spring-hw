package cyclone.otusspring.library.repository.datajpa;

import cyclone.otusspring.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

    @Query("select a from Author a " +
            "where lower(a.firstname) like lower('%'||?1||'%') " +
            "or lower(a.lastname) like lower('%'||?1||'%') " +
            "order by a.firstname, a.lastname")
    List<Author> findByName(String name);
}
