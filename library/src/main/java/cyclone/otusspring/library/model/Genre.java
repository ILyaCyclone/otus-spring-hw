package cyclone.otusspring.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Genre {
    private long genreId = -1;
    private String name;

    public Genre(String name) {
        Objects.requireNonNull(name, "genre name must not be null");

        this.name = name;
    }
}
