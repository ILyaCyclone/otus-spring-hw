package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.dto.Message;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.mapper.GenreMapper;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.GenreService;
import org.hamcrest.Matchers;
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
import static cyclone.otusspring.library.controller.GenreController.BASE_URL;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
@Import(GenreMapper.class)
class GenreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GenreMapper genreMapper;

    @MockBean
    GenreService genreServiceMock;

    @Test
    void genres() throws Exception {
        final List<Genre> genres = Arrays.asList(GENRE1, GENRE2, GENRE3);
        when(genreServiceMock.findAll()).thenReturn(genres);

        final List<GenreDto> genreDtoList = Arrays.asList(genreMapper.toGenreDto(GENRE1)
                , genreMapper.toGenreDto(GENRE2), genreMapper.toGenreDto(GENRE3));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genreDtoList", genreDtoList))
                .andExpect(view().name("genres"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(get(BASE_URL + "/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genreDto", new GenreDto()))
                .andExpect(view().name("genre-form"));
    }

    @Test
    void edit() throws Exception {
        when(genreServiceMock.findOne(GENRE1.getId())).thenReturn(GENRE1);

        mockMvc.perform(get(BASE_URL + "/" + GENRE1.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genreDto", genreMapper.toGenreDto(GENRE1)))
                .andExpect(view().name("genre-form"));
    }

    @Test
    void save() throws Exception {
        when(genreServiceMock.save(NEW_GENRE)).thenReturn(new Genre(NO_SUCH_ID, NEW_GENRE.getName()));

        mockMvc.perform(post(BASE_URL + "/save")
                .param("name", NEW_GENRE.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BASE_URL + "/" + NO_SUCH_ID));
    }

    @Test
    void delete() throws Exception {
        doNothing().when(genreServiceMock).delete(GENRE1.getId());

        mockMvc.perform(post(BASE_URL + "/" + GENRE1.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BASE_URL))
                .andExpect(flash().attribute("message"
                        , hasProperty("text"
                                , allOf(Matchers.startsWith("Genre ID"), Matchers.endsWith("deleted"))))
                );

        verify(genreServiceMock, times(1)).delete(GENRE1.getId());
    }

    @Test
    @DisplayName("has error message when genre not found")
    void edit_nonExistent() throws Exception {
        final String genreId = NO_SUCH_ID;
        final String errorMessage = "Genre ID " + genreId + " not found";
        when(genreServiceMock.findOne(genreId)).thenThrow(new NotFoundException(errorMessage));

        mockMvc.perform(get(BASE_URL + "/" + genreId))
                .andExpect(flash().attribute("message", new Message(errorMessage, Message.Type.ERROR)))
                .andExpect(redirectedUrl(BASE_URL));
    }
}