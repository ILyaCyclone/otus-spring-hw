package cyclone.otusspring.library.shell;

import cyclone.otusspring.library.dao.DataAccessProfiles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.table.Table;
import org.springframework.test.context.ActiveProfiles;

import static cyclone.otusspring.library.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(DataAccessProfiles.ACTIVE)
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
        String message = libraryCommands.createBook(NEW_BOOK.getTitle(), NEW_BOOK.getYear(), AUTHOR1.getAuthorId(), GENRE1.getGenreId());
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
        assertThat(table.getModel().getColumnCount() > 0).as("table should not be empty").isTrue();
    }
}