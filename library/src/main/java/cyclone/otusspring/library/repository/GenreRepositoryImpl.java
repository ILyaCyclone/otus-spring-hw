package cyclone.otusspring.library.repository;

import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.datajpa.GenreJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class GenreRepositoryImpl implements GenreRepository {

    private final GenreJpaRepository jpaRepository;

    public GenreRepositoryImpl(GenreJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public List<Genre> findAll() {
        return jpaRepository.findAllByOrderByName();
    }

    @Override
    public List<Genre> findByName(String name) {
        return jpaRepository.findByNameContainingIgnoreCaseOrderByName(name);
    }

    @Override
    public Genre findOne(long id) {
        return jpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre ID " + id + " not found"));
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        return jpaRepository.save(genre);
    }

    @Override
    @Transactional
    public void delete(long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Genre genre) {
        jpaRepository.delete(genre);
    }

    @Override
    public boolean exists(long id) {
        return jpaRepository.existsById(id);
    }
}
