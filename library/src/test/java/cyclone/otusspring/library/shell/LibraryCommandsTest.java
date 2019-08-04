package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.shell.table.Table;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(ResetStateExtension.class)
class LibraryCommandsTest {

    @Autowired
    LibraryCommands libraryCommands;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void createBook() {
        String message = libraryCommands.createBook(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), AUTHOR1.getId(), GENRE1.getId());

        assertThat(mongoTemplate.findAll(Book.class))
                .usingElementComparatorIgnoringFields("id")
                .contains(NEW_BOOK);
        assertThat(message).contains("successfully");
    }

    @Test
    void createAuthor() {
        String message = libraryCommands.createAuthor(NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland());

        assertThat(mongoTemplate.findAll(Author.class))
                .usingElementComparatorIgnoringFields("id")
                .contains(NEW_AUTHOR);
        assertThat(message).contains("successfully");
    }

    @Test
    void createGenre() {
        String message = libraryCommands.createGenre(NEW_GENRE.getName());

        assertThat(mongoTemplate.findAll(Genre.class))
                .usingElementComparatorIgnoringFields("id")
                .contains(NEW_GENRE);
        assertThat(message).contains("successfully");
    }

    @ParameterizedTest
    @DisplayName("listBooks (with --verbose and without)")
    @ValueSource(strings = {"true", "false"})
    // doesn't support boolean, so let's use string parameter
    @WithMockUser(username = "user1", roles = {"USER"})
    void listBooksAsUser1(String verboseString) {
        int expectedRowCount = 3; // ACL allows to see only 3 of 5 books to user1
        assertTableRowCount(libraryCommands.listBooks(Boolean.valueOf(verboseString)), expectedRowCount);
    }

    @Test
    @WithMockUser(username = "user2", roles = {"USER"})
    void listBooksAsUser2() {
        int expectedRowCount = 2; // ACL allows to see only 2 of 5 books to user2
        assertTableRowCount(libraryCommands.listBooks(false), expectedRowCount);
    }

    @Test
    void listAuthors() {
        assertTableRowCount(libraryCommands.listAuthors(), 4);
    }

    @Test
    void listGenres() {
        assertTableRowCount(libraryCommands.listGenres(), 5);
    }

    private void assertTableRowCount(Table table, int expectedRowCount) {
        assertThat(table.getModel().getRowCount())
                .as("table should contain " + expectedRowCount + " number of rows (not counting header)")
                .isEqualTo(expectedRowCount + 1); // +1 for header
    }
}