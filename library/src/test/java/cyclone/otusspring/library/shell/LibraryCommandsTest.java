package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dbteststate.ResetStateExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.table.Table;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(ResetStateExtension.class)
class LibraryCommandsTest {

    @Autowired
    LibraryCommands libraryCommands;

    @ParameterizedTest
    @DisplayName("listBooks (with --verbose and without)")
    @ValueSource(strings = {"true", "false"})
        // doesn't support booleans, so let's use strings
    void listBooks(String verboseString) {
        assertTableNotEmpty(libraryCommands.listBooks(Boolean.valueOf(verboseString)));
    }

    @Test
    void createBook() {
        String message = libraryCommands.createBook(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), AUTHOR1.getId(), GENRE1.getId());
        assertThat(message).contains("successfully");
    }

    @Test
    void createAuthor() {
        String message = libraryCommands.createAuthor(NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland());
        assertThat(message).contains("successfully");
    }

    @Test
    void createGenre() {
        String message = libraryCommands.createGenre(NEW_GENRE.getName());
        assertThat(message).contains("successfully");
    }

    @Test
    void listAuthors() {
        assertTableNotEmpty(libraryCommands.listAuthors());
    }

    @Test
    void listGenres() {
        assertTableNotEmpty(libraryCommands.listGenres());
    }



    private void assertTableNotEmpty(Table table) {
        assertThat(table.getModel().getColumnCount()).as("table should not be empty").isGreaterThan(0);
    }
}