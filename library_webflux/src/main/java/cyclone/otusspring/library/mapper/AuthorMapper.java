package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.model.Author;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    public Author toAuthor(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getFirstname(), authorDto.getLastname(), authorDto.getHomeland());
    }

    public AuthorDto toAuthorDto(Author author) {
        return new AuthorDto(author.getId(), author.getFirstname(), author.getLastname(), author.getHomeland());
    }

    public List<AuthorDto> toAuthorDtoList(Collection<Author> authors) {
        return authors.stream()
                .map(this::toAuthorDto)
                .collect(Collectors.toList());
    }

}
