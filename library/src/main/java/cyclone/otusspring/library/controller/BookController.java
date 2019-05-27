package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookMapper bookMapper;

    @GetMapping
    public String books(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books";
    }


    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("bookDto", new BookDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book-form";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable(name = "id") String id) {
        model.addAttribute("bookDto", bookMapper.toDto(bookService.findOne(id)));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "book-form";
    }

    @PostMapping("/save")
    public String save(BookDto bookDto, RedirectAttributes redirectAttributes) {
        if (bookDto.getId() != null && bookDto.getId().length() == 0) {
            bookDto.setId(null);
        }
        bookService.save(bookDto);
        redirectAttributes.addFlashAttribute("message", "Book saved");
        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes) {
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Book ID " + id + "deleted");
        return "redirect:/books";
    }

}
