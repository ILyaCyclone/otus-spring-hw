package cyclone.otusspring.library.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class AuthorDto {
    private final String firstname;
    private final String lastname;
    private final String homeland;

    public AuthorDto(String firstname, String lastname) {
        this(firstname, lastname, null);
    }
    public AuthorDto(String firstname, String lastname, String homeland) {
        Objects.requireNonNull(firstname, "author firstname must not be null");
        Objects.requireNonNull(lastname, "author lastname must not be null");

        this.firstname = firstname;
        this.lastname = lastname;
        this.homeland = homeland;
    }
}
