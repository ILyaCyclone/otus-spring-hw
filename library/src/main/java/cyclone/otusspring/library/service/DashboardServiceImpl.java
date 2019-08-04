package cyclone.otusspring.library.service;

import cyclone.otusspring.library.repository.AuthorRepository;
import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public long getAuthorCount() {
        return authorRepository.count();
    }

    @Override
    public long getBookCount() {
        return bookRepository.count();
    }

    @Override
    public long getGenreCount() {
        return genreRepository.count();
    }
}
