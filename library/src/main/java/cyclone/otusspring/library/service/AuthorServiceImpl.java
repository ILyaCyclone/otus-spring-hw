package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dao.AuthorDao;
import cyclone.otusspring.library.model.Author;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public List<Author> findAll() {
        return authorDao.findAll();
    }
}
