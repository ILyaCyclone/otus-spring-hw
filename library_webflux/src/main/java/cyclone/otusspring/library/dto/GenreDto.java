package cyclone.otusspring.library.dto;

import lombok.Data;

@Data
public class GenreDto {
    private String id;
    private String name;

    public GenreDto() {
    }

    public GenreDto(String name) {
        this.name = name;
    }

    public GenreDto(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
