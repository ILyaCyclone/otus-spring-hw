package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.dto.Message;
import cyclone.otusspring.library.exceptions.NotFoundException;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.service.AuthorService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static cyclone.otusspring.library.TestData.*;
import static cyclone.otusspring.library.controller.AuthorController.BASE_URL;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
@Import(AuthorMapper.class)
class AuthorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthorMapper authorMapper;

    @MockBean
    AuthorService authorServiceMock;

    @Test
    void authors() throws Exception {
        final List<Author> authors = Arrays.asList(AUTHOR1, AUTHOR2, AUTHOR3);
        when(authorServiceMock.findAll()).thenReturn(authors);

        final List<AuthorDto> authorDtoList = Arrays.asList(authorMapper.toAuthorDto(AUTHOR1)
                , authorMapper.toAuthorDto(AUTHOR2), authorMapper.toAuthorDto(AUTHOR3));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authorDtoList", authorDtoList))
                .andExpect(view().name("authors"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(get(BASE_URL + "/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authorDto", new AuthorDto()))
                .andExpect(view().name("author-form"));
    }

    @Test
    void edit() throws Exception {
        when(authorServiceMock.findOne(AUTHOR1.getId())).thenReturn(AUTHOR1);

        mockMvc.perform(get(BASE_URL + "/" + AUTHOR1.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authorDto", authorMapper.toAuthorDto(AUTHOR1)))
                .andExpect(view().name("author-form"));
    }


    @Test
    void save() throws Exception {
        final Author savedAuthor = new Author(NO_SUCH_ID, NEW_AUTHOR.getFirstname(), NEW_AUTHOR.getLastname(), NEW_AUTHOR.getHomeland());
        when(authorServiceMock.save(NEW_AUTHOR)).thenReturn(savedAuthor);

        mockMvc.perform(post(BASE_URL + "/save")
                .param("firstname", NEW_AUTHOR.getFirstname())
                .param("lastname", NEW_AUTHOR.getLastname())
                .param("homeland", NEW_AUTHOR.getHomeland())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", new Message("Author saved")))
                .andExpect(redirectedUrl(BASE_URL + "/" + savedAuthor.getId()));
    }

    @Test
    void delete() throws Exception {
        doNothing().when(authorServiceMock).delete(AUTHOR1.getId());

        mockMvc.perform(post(BASE_URL + "/" + AUTHOR1.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BASE_URL))
                .andExpect(flash().attribute("message"
                        , hasProperty("text"
                                , allOf(Matchers.startsWith("Author ID"), Matchers.endsWith("deleted"))))
                );

        verify(authorServiceMock, times(1)).delete(AUTHOR1.getId());
    }


    @Test
    @DisplayName("has error message when author not found")
    void edit_nonExistent() throws Exception {
        final String authorId = NO_SUCH_ID;
        final String errorMessage = "Author ID " + authorId + " not found";
        when(authorServiceMock.findOne(authorId)).thenThrow(new NotFoundException(errorMessage));

        mockMvc.perform(get(BASE_URL + "/" + authorId))
                .andExpect(flash().attribute("message", new Message(errorMessage, Message.Type.ERROR)))
                .andExpect(redirectedUrl(BASE_URL));
    }
}