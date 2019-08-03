package cyclone.otusspring.library.security;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "roles")
@Data
public class Role {
    @Id
    private String id;

    @Field("name")
    @Indexed(name = "roles_unique", unique = true)
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
}
