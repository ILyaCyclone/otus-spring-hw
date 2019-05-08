package cyclone.otusspring.library.repository.datajpa;

import cyclone.otusspring.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreJpaRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAllByOrderByName();

    List<Genre> findByNameContainingIgnoreCaseOrderByName(String name);
}
