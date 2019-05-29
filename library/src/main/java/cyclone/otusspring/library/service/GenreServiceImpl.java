package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.mapper.GenreMapper;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    public Genre findOne(String id) {
        return genreRepository.findOne(id);
    }

    @Override
    public Genre save(GenreDto genreDto) {
        return genreRepository.save(genreMapper.toGenre(genreDto));
    }

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public void delete(String id) {
        genreRepository.delete(id);
    }
}
