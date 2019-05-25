package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public String authors(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors";
    }

}
