package cyclone.otusspring.library.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.repository.AuthorRepository;
import cyclone.otusspring.library.service.AuthorService;
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
import static cyclone.otusspring.library.controller.rest.AuthorRestController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorRestController.class)
@Import(AuthorMapper.class)
class AuthorRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorService authorService;

    @MockBean
    AuthorRepository authorRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthorMapper authorMapper;


    @Test
    void findAll() throws Exception {
        when(authorService.findAll()).thenReturn(Arrays.asList(AUTHOR1, AUTHOR3, AUTHOR2));

        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(authorService).findAll();

        List<AuthorDto> actualAuthorDtoList = JsonUtils.readListFromMvcResult(objectMapper, mvcResult, AuthorDto.class);
        assertThat(actualAuthorDtoList)
                .usingFieldByFieldElementComparator()
                .containsExactly(authorMapper.toAuthorDto(AUTHOR1), authorMapper.toAuthorDto(AUTHOR3), authorMapper.toAuthorDto(AUTHOR2));

    }

    @Test
    void findOne() throws Exception {
        when(authorService.findOne("1")).thenReturn(AUTHOR1);

        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL + "/1"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(authorService).findOne("1");

        AuthorDto actualAuthorDto = JsonUtils.readValueFromMvcResult(objectMapper, mvcResult, AuthorDto.class);
        assertThat(actualAuthorDto)
                .isEqualToComparingFieldByField(AUTHOR1);
    }

    @Test
    void create() throws Exception {
        final AuthorDto authorDtoToCreate = new AuthorDto("new firstname", "new lastname", "new homeland");
        Author authorToCreate = authorMapper.toAuthor(authorDtoToCreate);
        Author createdAuthor = authorMapper.toAuthor(authorDtoToCreate);
        createdAuthor.setId("generated-new-id");

        when(authorService.save(authorMapper.toAuthor(authorDtoToCreate))).thenReturn(createdAuthor);

        MvcResult mvcResult =
                mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.writeValue(objectMapper, authorDtoToCreate)))

                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn();

        verify(authorService).save(authorToCreate);

        AuthorDto createdAuthorDto = JsonUtils.readValueFromMvcResult(objectMapper, mvcResult, AuthorDto.class);

        assertThat(createdAuthorDto.getId()).isNotNull();
        assertThat(createdAuthorDto).isEqualToIgnoringGivenFields(authorDtoToCreate, "id");
    }

    @Test
    void update() throws Exception {
        final AuthorDto authorDtoToUpdate = new AuthorDto("1", "upd fistname", "upd lastname", "upd homeland");
        Author authorToUpdate = authorMapper.toAuthor(authorDtoToUpdate);

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValue(objectMapper, authorDtoToUpdate)))

                .andExpect(status().isNoContent());

        verify(authorService).save(authorToUpdate);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(authorService).delete("1");
    }
}