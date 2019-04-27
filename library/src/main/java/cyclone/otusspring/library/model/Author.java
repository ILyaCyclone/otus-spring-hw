package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "author")
@Data
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "homeland")
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
