package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.dto.Message;
import cyclone.otusspring.library.mapper.AuthorMapper;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

import static cyclone.otusspring.library.controller.AuthorController.BASE_URL;

@RequiredArgsConstructor
@Controller
@RequestMapping(BASE_URL)
public class AuthorController {
    static final String BASE_URL = "/authors";
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception ex, RedirectAttributes redirectAttributes) {
        logger.error("Request: " + req.getRequestURL() + " raised " + ex);

        redirectAttributes.addFlashAttribute("message", new Message(ex.getMessage(), Message.Type.ERROR));
        return getRedirectToAuthors();
    }



    @GetMapping
    public String authors(Model model) {
        model.addAttribute("authors", authorService.findAll().stream()
                .map(authorMapper::toAuthorDto)
                .collect(Collectors.toList()));
        return "authors";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("authorDto", new AuthorDto());
        return "author-form";
    }

    @GetMapping("/{id}")
    public String edit(Model model, @PathVariable(name = "id") String id) {
        model.addAttribute("authorDto", authorMapper.toAuthorDto(authorService.findOne(id)));
        return "author-form";
    }

    @PostMapping("/save")
    public String save(AuthorDto authorDto, RedirectAttributes redirectAttributes) {
        if (authorDto.getId() != null && authorDto.getId().length() == 0) {
            authorDto.setId(null);
        }
        Author savedAuthor = authorService.save(authorDto);
        redirectAttributes.addFlashAttribute("message", new Message("Author saved"));

        return "redirect:/authors/" + savedAuthor.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes) {
        authorService.delete(id);
        redirectAttributes.addFlashAttribute("message", new Message("Author ID " + id + " deleted"));
        return getRedirectToAuthors();
    }



    static String getRedirectToAuthors() {
        return "redirect:/authors";
    }
}
