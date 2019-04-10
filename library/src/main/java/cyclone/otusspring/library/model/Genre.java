package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "genre")
@Data
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long genreId;
    private String name;

    public Genre(long genreId) {
        this.genreId = genreId;
    }

    public Genre(String name) {
        this(null, name);
    }

    public Genre(Long id, String name) {
        Objects.requireNonNull(name, "genre name must not be null");
        this.genreId = id;
        this.name = name;
    }
}
