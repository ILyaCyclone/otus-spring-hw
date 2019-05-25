package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.service.GenreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public String genres(Model model) {
        model.addAttribute("genres", genreService.findAll());
        return "genres";
    }

}
