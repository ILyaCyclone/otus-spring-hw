package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dao.GenreDao;
import cyclone.otusspring.library.model.Genre;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> findAll() {
        return genreDao.findAll();
    }
}
