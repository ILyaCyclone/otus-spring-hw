package cyclone.otusspring.library.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class AuthorDto {
    private String id;
    private String firstname;
    private String lastname;
    private String homeland;

    public AuthorDto() {
    }

    public AuthorDto(String firstname, String lastname, String homeland) {
        this(null, firstname, lastname, homeland);
    }

    public AuthorDto(String id, String firstname, String lastname, String homeland) {
        Objects.requireNonNull(firstname, "author firstname must not be null");
        Objects.requireNonNull(lastname, "author lastname must not be null");

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.homeland = homeland;
    }
}
