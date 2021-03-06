package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = "authors")
@CompoundIndex(name = "authors_unique"
        , def = "{'firstname' : 1, 'lastname' : 1, 'homeland' : 1}"
        , unique = true)
@Data
@NoArgsConstructor
public class Author {
    @Id
    private String id;

    @Field("firstname")
    private String firstname;

    @Field("lastname")
    private String lastname;

    @Field("homeland")
    private String homeland;


    public Author(String id) {
        this.id = id;
    }

    public Author(String firstname, String lastname, String homeland) {
        this(null, firstname, lastname, homeland);
    }

    public Author(String id, String firstname, String lastname, String homeland) {
        Objects.requireNonNull(firstname, "author firstname must not be null");
        Objects.requireNonNull(lastname, "author lastname must not be null");

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.homeland = homeland;
    }
}
