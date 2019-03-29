package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Author {
    private long authorId = -1;
    private String firstname;
    private String lastname;
    private String homeland;

    public Author(String firstname, String lastname, String homeland) {
        Objects.requireNonNull(firstname, "author firstname must not be null");
        Objects.requireNonNull(lastname, "author lastname must not be null");

        this.firstname = firstname;
        this.lastname = lastname;
        this.homeland = homeland;
    }
}
