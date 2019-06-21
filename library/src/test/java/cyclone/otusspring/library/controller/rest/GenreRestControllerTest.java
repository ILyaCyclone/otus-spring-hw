package cyclone.otusspring.library.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.mapper.GenreMapper;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.repository.GenreRepository;
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

import java.util.Arrays;
import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static cyclone.otusspring.library.controller.rest.GenreRestController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GenreRestController.class)
@Import(GenreMapper.class)
class GenreRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    GenreService genreService;

    @MockBean
    GenreRepository genreRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GenreMapper genreMapper;


    @Test
    void findAll() throws Exception {
        when(genreService.findAll()).thenReturn(Arrays.asList(GENRE1, GENRE3, GENRE2));

        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(genreService).findAll();

        List<GenreDto> actualGenreDtoList = JsonUtils.readListFromMvcResult(objectMapper, mvcResult, GenreDto.class);
        assertThat(actualGenreDtoList)
                .usingFieldByFieldElementComparator()
                .containsExactly(genreMapper.toGenreDto(GENRE1), genreMapper.toGenreDto(GENRE3), genreMapper.toGenreDto(GENRE2));

    }

    @Test
    void findOne() throws Exception {
        when(genreService.findOne("1")).thenReturn(GENRE1);

        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL + "/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(genreService).findOne("1");

        GenreDto actualGenreDto = JsonUtils.readValueFromMvcResult(objectMapper, mvcResult, GenreDto.class);
        assertThat(actualGenreDto)
                .isEqualToComparingFieldByField(GENRE1);
    }

    @Test
    void create() throws Exception {
        final GenreDto genreDtoToCreate = new GenreDto("new name");
        Genre genreToCreate = genreMapper.toGenre(genreDtoToCreate);
        Genre createdGenre = genreMapper.toGenre(genreDtoToCreate);
        createdGenre.setId("generated-new-id");

        when(genreService.save(genreMapper.toGenre(genreDtoToCreate))).thenReturn(createdGenre);

        MvcResult mvcResult =
                mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.writeValue(objectMapper, genreDtoToCreate)))

                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(genreService).save(genreToCreate);

        GenreDto createdGenreDto = JsonUtils.readValueFromMvcResult(objectMapper, mvcResult, GenreDto.class);

        assertThat(createdGenreDto.getId()).isNotNull();
        assertThat(createdGenreDto).isEqualToIgnoringGivenFields(genreDtoToCreate, "id");
    }

    @Test
    void update() throws Exception {
        final GenreDto genreDtoToUpdate = new GenreDto("1", "upd name");
        Genre genreToUpdate = genreMapper.toGenre(genreDtoToUpdate);

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValue(objectMapper, genreDtoToUpdate)))

                .andExpect(status().isNoContent());

        verify(genreService).save(genreToUpdate);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(genreService).delete("1");
    }
}