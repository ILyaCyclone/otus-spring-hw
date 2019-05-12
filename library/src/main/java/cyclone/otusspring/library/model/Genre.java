package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Objects;

@Document("genres")
@Data
@NoArgsConstructor
public class Genre {
    @Id
    private String id;

    @Field("name")
    private String name;

    public Genre(String name) {
        this(null, name);
    }

    public Genre(String id, String name) {
        Objects.requireNonNull(name, "genre name must not be null");
        this.id = id;
        this.name = name;
    }
}