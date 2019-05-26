package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.dto.AuthorDto;
import cyclone.otusspring.library.dto.AuthorMapper;
import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @GetMapping
    public String authors(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("author", new AuthorDto());
        return "author-form";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable(name = "id") String id) {
        model.addAttribute("author", authorService.findOne(id));
        return "author-form";
    }

    @PostMapping("/save")
    public String save(AuthorDto authorDto, RedirectAttributes redirectAttributes) {
        if (authorDto.getId() != null && authorDto.getId().length() == 0) {
            authorDto.setId(null);
        }
        if (authorDto.getId() == null) {
            Author createdAuthor = authorService.create(authorDto);
            redirectAttributes.addFlashAttribute("message", "Author created with ID " + createdAuthor.getId());
        } else {
            authorService.save(authorMapper.toAuthor(authorDto));
            redirectAttributes.addFlashAttribute("message", "Author saved");
        }
        return "redirect:/authors";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes) {
        authorService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Author ID " + id + "deleted");
        return "redirect:/authors";
    }

}
