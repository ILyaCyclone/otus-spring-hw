package cyclone.otusspring.library.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyclone.otusspring.library.dto.BookFormDto;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.mapper.CommentMapper;
import cyclone.otusspring.library.mapper.GenreMapper;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static cyclone.otusspring.library.TestData.*;
import static cyclone.otusspring.library.controller.rest.BookFormdataRestController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookFormdataRestController.class)
@Import({BookMapper.class, AuthorMapper.class, GenreMapper.class, CommentMapper.class})
class BookFormdataRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

    @MockBean
    AuthorService authorService; // needed in bookMapper

    @MockBean
    GenreService genreService; // needed in bookMapper

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    AuthorMapper authorMapper;

    @Autowired
    GenreMapper genreMapper;



    @Test
    void getBookFormData() throws Exception {
        when(bookService.findOne("1")).thenReturn(Mono.just(BOOK1));
        when(authorService.findAll()).thenReturn(Flux.just(AUTHOR1, AUTHOR2));
        when(genreService.findAll()).thenReturn(Flux.just(GENRE1, GENRE2));

        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL + "/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(bookService).findOne("1");

        BookFormDto bookFormDto = JsonUtils.readValueFromMvcResult(objectMapper, mvcResult, BookFormDto.class);
        assertThat(bookFormDto.getBookDto())
                .isEqualTo(bookMapper.toBookDto(BOOK1));
        assertThat(bookFormDto.getAllAuthors())
                .containsExactly(authorMapper.toAuthorDto(AUTHOR1), authorMapper.toAuthorDto(AUTHOR2));
        assertThat(bookFormDto.getAllGenres())
                .containsExactly(genreMapper.toGenreDto(GENRE1), genreMapper.toGenreDto(GENRE2));
    }

    @Test
    void getNewBookFormData() throws Exception {
        when(authorService.findAll()).thenReturn(Flux.just(AUTHOR1, AUTHOR2));
        when(genreService.findAll()).thenReturn(Flux.just(GENRE1, GENRE2));

        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL + "/new"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(bookService, never()).findOne(any());

        BookFormDto bookFormDto = JsonUtils.readValueFromMvcResult(objectMapper, mvcResult, BookFormDto.class);
        assertThat(bookFormDto.getBookDto()).isNull();
        assertThat(bookFormDto.getAllAuthors())
                .containsExactly(authorMapper.toAuthorDto(AUTHOR1), authorMapper.toAuthorDto(AUTHOR2));
        assertThat(bookFormDto.getAllGenres())
                .containsExactly(genreMapper.toGenreDto(GENRE1), genreMapper.toGenreDto(GENRE2));
    }
}