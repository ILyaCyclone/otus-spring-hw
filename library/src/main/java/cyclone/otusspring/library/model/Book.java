package cyclone.otusspring.library.model;


import cyclone.otusspring.library.exceptions.NotFoundException;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Document(collection = "books")
@CompoundIndex(name = "books_unique"
        , def = "{'title' : 1, 'year' : 1, 'author': 1}"
        , unique = true)
@Data
@NoArgsConstructor
public class Book {

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("year")
    private Integer year;

    @DBRef
    @Field("author")
    private Author author;

    @DBRef
    @Field("genre")
    private Genre genre;

    @Field("comments")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<Comment> comments = new ArrayList<>();


    public Book(String title, Integer year) {
        this.title = title;
        this.year = year;
    }

    public Book(String title, Integer year, Author author, Genre genre) {
        this(null, title, year, author, genre);
    }

    public Book(String id, String title, Integer year, Author author, Genre genre) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("book title must not be empty");
        }

        this.id = id;
        this.title = title;
        this.year = year;
        this.author = author;
        this.genre = genre;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(Comment comment) {
        if (comment.getId() == null) {
            comment.setId(ObjectId.get().toString());
        }
        comments.add(comment);
    }

    public void addComments(Collection<Comment> comments) {
        comments.forEach(this::addComment);
    }

    public void removeComment(String commentId) {
        Comment commentToRemove = comments.stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Comment " + commentId + " not found"));
        comments.remove(commentToRemove);
    }
}
