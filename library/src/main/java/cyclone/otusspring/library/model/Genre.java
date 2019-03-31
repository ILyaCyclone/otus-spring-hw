package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Genre {
    private Long genreId;
    private String name;

    public Genre(String name) {
        this(null, name);
    }

    public Genre(Long id, String name) {
        Objects.requireNonNull(name, "genre name must not be null");
        this.genreId = id;
        this.name = name;
    }
}
