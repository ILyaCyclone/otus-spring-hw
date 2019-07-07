package cyclone.otusspring.library.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.BookListElementDto;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.mapper.CommentMapper;
import cyclone.otusspring.library.mapper.GenreMapper;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static cyclone.otusspring.library.controller.rest.BookRestController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookRestController.class)
@Import({BookMapper.class, AuthorMapper.class, GenreMapper.class, CommentMapper.class})
class BookRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

    @MockBean
    AuthorService authorService; // needed in bookMapper

    @MockBean
    GenreService genreService; // needed in bookMapper

    @MockBean
    BookRepository bookRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookMapper bookMapper;


    @Test
    void findAll() throws Exception {
        when(bookService.findAll()).thenReturn(Arrays.asList(BOOK1, BOOK2));

        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(bookService).findAll();

        List<BookListElementDto> actualBookDtoList = JsonUtils.readListFromMvcResult(objectMapper, mvcResult, BookListElementDto.class);
        assertThat(actualBookDtoList)
                .usingFieldByFieldElementComparator()
                .containsExactly(bookMapper.toBooksElementDto(BOOK1), bookMapper.toBooksElementDto(BOOK2));
    }

    @Test
    void findOne() throws Exception {
        when(bookService.findOne("1")).thenReturn(BOOK1);

        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL + "/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(bookService).findOne("1");

        BookDto actualBookDto = JsonUtils.readValueFromMvcResult(objectMapper, mvcResult, BookDto.class);
        assertThat(actualBookDto)
                .isEqualToComparingOnlyGivenFields(BOOK1, "title", "year");
        assertThat(actualBookDto.getAuthorId()).isEqualTo(BOOK1.getAuthor().getId());
        assertThat(actualBookDto.getGenreId()).isEqualTo(BOOK1.getGenre().getId());
    }

    @Test
    void create() throws Exception {
        when(authorService.findOne("1")).thenReturn(Mono.just(AUTHOR1));
        when(genreService.findOne("1")).thenReturn(Mono.just(GENRE1));

        final BookDto bookDtoToCreate = new BookDto("new title", 2000, "1", "1");
        Book bookToCreate = bookMapper.toBook(bookDtoToCreate);
        Book createdBook = bookMapper.toBook(bookDtoToCreate);
        createdBook.setId("generated-new-id");

        when(bookService.save(bookMapper.toBook(bookDtoToCreate))).thenReturn(createdBook);

        MvcResult mvcResult =
                mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.writeValue(objectMapper, bookDtoToCreate)))

                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(bookService).save(bookToCreate);

        BookDto createdBookDto = JsonUtils.readValueFromMvcResult(objectMapper, mvcResult, BookDto.class);

        assertThat(createdBookDto.getId()).isNotNull();
        assertThat(createdBookDto).isEqualToIgnoringGivenFields(bookDtoToCreate, "id");
    }

    @Test
    void update() throws Exception {
        when(authorService.findOne("1")).thenReturn(Mono.just(AUTHOR1));
        when(genreService.findOne("1")).thenReturn(Mono.just(GENRE1));

        final BookDto bookDtoToUpdate = new BookDto("1", "upd title", 2000, "1", "1");
        Book bookToUpdate = bookMapper.toBook(bookDtoToUpdate);

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValue(objectMapper, bookDtoToUpdate)))

                .andExpect(status().isNoContent());

        verify(bookService).save(bookToUpdate);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(bookService).delete("1");
    }

    @Test
    void saveComment() throws Exception {
        when(bookService.findOne("1")).thenReturn(BOOK1);
        CommentDto commentDtoToCreate = new CommentDto("1", "new commentator", "new text");

        mockMvc.perform(post(BASE_URL + "/1/comments/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValue(objectMapper, commentDtoToCreate)))

                .andExpect(status().isCreated())
                .andReturn();

        verify(bookService).findOne("1");
        verify(bookService).save((Book) any());
    }
}