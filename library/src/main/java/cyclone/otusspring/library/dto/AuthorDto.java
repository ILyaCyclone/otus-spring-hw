package cyclone.otusspring.library.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorDto implements Serializable {
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
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.homeland = homeland;
    }
}
