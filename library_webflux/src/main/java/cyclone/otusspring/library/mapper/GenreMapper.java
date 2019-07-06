package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.model.Genre;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreMapper {

    public Genre toGenre(GenreDto genreDto) {
        return new Genre(genreDto.getId(), genreDto.getName());
    }

    public GenreDto toGenreDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public List<GenreDto> toGenreDtoList(Collection<Genre> genres) {
        return genres.stream()
                .map(this::toGenreDto)
                .collect(Collectors.toList());
    }

}
