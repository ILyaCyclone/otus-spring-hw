package cyclone.otusspring.library.controller.view;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.dto.Message;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.mapper.CommentMapper;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.BookWithoutComments;
import cyclone.otusspring.library.service.AuthenticationServiceImpl;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static cyclone.otusspring.library.controller.view.BookController.BASE_URL;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@WithMockUser(username = "user1")
@Import({BookMapper.class, CommentMapper.class, AuthenticationServiceImpl.class})
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserDetailsService userDetailsService;

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
    void booksView() throws Exception {
        final List<Book> books = Arrays.asList(BOOK1, BOOK2, BOOK3);
        final List<BookListElementDto> bookListElementDtoList = bookMapper.toBooksElementDtoList(books);
        when(bookServiceMock.findAll()).thenReturn(books);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("booksElementDtoList", bookListElementDtoList))
                .andExpect(view().name("books"));
    }

    @Test
    void createView() throws Exception {
        mockMvc.perform(get(BASE_URL + "/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDto", new BookDto()))
                .andExpect(view().name("book-form"));
    }

    @Test
    void editView() throws Exception {
        when(bookServiceMock.findOne(BOOK1.getId())).thenReturn(BOOK1);

        mockMvc.perform(get(BASE_URL + "/" + BOOK1.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDto", bookMapper.toBookDto(BOOK1)))
                .andExpect(view().name("book-form"));
    }

    @Test
    void save() throws Exception {
        final BookWithoutComments bookToSave = new BookWithoutComments(null, NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor(), NEW_BOOK.getGenre());
        final BookWithoutComments savedBook = new BookWithoutComments(NO_SUCH_ID, NEW_BOOK.getTitle(), NEW_BOOK.getYear(), NEW_BOOK.getAuthor(), NEW_BOOK.getGenre());
        // AuthorService and GenreService are used by BookMapper
        when(authorServiceMock.findOne(NEW_BOOK.getAuthor().getId())).thenReturn(NEW_BOOK.getAuthor());
        when(genreServiceMock.findOne(NEW_BOOK.getGenre().getId())).thenReturn(NEW_BOOK.getGenre());
        when(bookServiceMock.save(bookToSave)).thenReturn(savedBook);

        mockMvc.perform(post(BASE_URL + "/save")
                .param("title", NEW_BOOK.getTitle())
                .param("year", String.valueOf(NEW_BOOK.getYear()))
                .param("authorId", NEW_BOOK.getAuthor().getId())
                .param("genreId", NEW_BOOK.getGenre().getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", new Message("Book saved")))
                .andExpect(redirectedUrl(BASE_URL + "/" + NO_SUCH_ID));
    }

    @Test
    void delete() throws Exception {
        doNothing().when(bookServiceMock).delete(BOOK1.getId());

        mockMvc.perform(post(BASE_URL + "/" + BOOK1.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BASE_URL))
                .andExpect(flash().attribute("message"
                        , hasProperty("text"
                                , allOf(Matchers.startsWith("Book ID"), Matchers.endsWith("deleted"))))
                );

        verify(bookServiceMock, times(1)).delete(BOOK1.getId());
    }

    @Test
    void saveComment() throws Exception {
        when(bookServiceMock.findOne(BOOK1.getId())).thenReturn(BOOK1);

        mockMvc.perform(post(BASE_URL + "/" + BOOK1.getId() + "/comments/save")
                .param("text", NEW_COMMENT.getText())
                .param("commentator", NEW_COMMENT.getCommentator())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", new Message("Comment saved")))
                .andExpect(redirectedUrl(BASE_URL + "/" + BOOK1.getId()));
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