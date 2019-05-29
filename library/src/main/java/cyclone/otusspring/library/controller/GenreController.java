package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.dto.GenreDto;
import cyclone.otusspring.library.mapper.GenreMapper;
import cyclone.otusspring.library.model.Genre;
import cyclone.otusspring.library.service.GenreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @GetMapping
    public String genres(Model model) {
        model.addAttribute("genres", genreService.findAll());
        return "genres";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("genreDto", new GenreDto());
        return "genre-form";
    }

    @GetMapping("/{id}")
    public String edit(Model model, @PathVariable(name = "id") String id) {
        model.addAttribute("genreDto", genreMapper.toGenreDto(genreService.findOne(id)));
        return "genre-form";
    }

    @PostMapping("/save")
    public String save(GenreDto genreDto, RedirectAttributes redirectAttributes) {
        if (genreDto.getId() != null && genreDto.getId().length() == 0) {
            genreDto.setId(null);
        }
        Genre savedGenre = genreService.save(genreDto);
        redirectAttributes.addFlashAttribute("message", "Genre saved");

        return "redirect:/genres/" + savedGenre.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes) {
        genreService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Genre ID " + id + " deleted");
        return "redirect:/genres";
    }

}
