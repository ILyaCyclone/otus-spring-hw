package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Author {
    private Long authorId;
    private String firstname;
    private String lastname;
    private String homeland;

    public Author(long authorId) {
        this.authorId = authorId;
    }

    public Author(String firstname, String lastname, String homeland) {
        this(null, firstname, lastname, homeland);
    }

    public Author(Long id, String firstname, String lastname, String homeland) {
        Objects.requireNonNull(firstname, "author firstname must not be null");
        Objects.requireNonNull(lastname, "author lastname must not be null");

        this.authorId = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.homeland = homeland;
    }
}
