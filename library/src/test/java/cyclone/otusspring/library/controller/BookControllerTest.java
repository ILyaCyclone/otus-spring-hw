package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.dto.Message;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.mapper.CommentMapper;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static cyclone.otusspring.library.controller.BookController.BASE_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import({BookMapper.class, CommentMapper.class})
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BookMapper bookMapper;
    @Autowired
    CommentMapper commentMapper;

    @MockBean
    BookService bookServiceMock;
    @MockBean
    AuthorService authorServiceMock;
    @MockBean
    GenreService genreServiceMock;

    @Test
    void books() throws Exception {
        final List<Book> books = Arrays.asList(BOOK1, BOOK2, BOOK3);
        final List<BookListElementDto> bookListElementDtoList = bookMapper.toBooksElementDtoList(books);
        when(bookServiceMock.findAll()).thenReturn(books);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("booksElementDtoList", bookListElementDtoList))
                .andExpect(view().name("books"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(get(BASE_URL + "/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDto", new BookDto()))
                .andExpect(view().name("book-form"));
    }

    @Test
    void edit() {
        Assertions.fail("not implemented");
    }

    @Test
    void save() {
        Assertions.fail("not implemented");
    }

    @Test
    void delete() throws Exception {
        doNothing().when(bookServiceMock).delete(BOOK1.getId());

        mockMvc.perform(post(BASE_URL + "/" + BOOK1.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BASE_URL))
                .andExpect(flash().attribute("message"
                        , hasProperty("text"
                                , allOf(startsWith("Book ID"), endsWith("deleted"))))
                );
    }

    @Test
    void saveComment() {
        Assertions.fail("not implemented");
    }

    @Test
    @DisplayName("has error message when book not found")
    void edit_nonExistent() throws Exception {
        final String bookId = NO_SUCH_ID;
        final String errorMessage = "Book ID " + bookId + " not found";
        when(bookServiceMock.findOne(bookId)).thenThrow(new NotFoundException(errorMessage));

        mockMvc.perform(get(BASE_URL + "/" + bookId))
                .andExpect(flash().attribute("message", new Message(errorMessage, Message.Type.ERROR)))
                .andExpect(redirectedUrl(BookController.getRedirectToBooks().replace("redirect:", "")));
    }
}