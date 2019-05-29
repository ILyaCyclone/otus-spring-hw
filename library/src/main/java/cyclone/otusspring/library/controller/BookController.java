package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
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

import java.util.stream.Collectors;

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
        addAuthorsAndGenres(model);
        return "book-form";
    }

    @GetMapping("/{id}")
    public String edit(Model model, @PathVariable(name = "id") String id) {
        Book book = bookService.findOne(id);
        model.addAttribute("bookDto", bookMapper.toDto(book));
        model.addAttribute("comments", book.getComments().stream()
                .map(comment -> new CommentDto(book.getId(), comment.getCommentator(), comment.getText(), comment.getDate()))
                .collect(Collectors.toList())
        );

        addAuthorsAndGenres(model);
        return "book-form";
    }

    @PostMapping("/save")
    public String save(BookDto bookDto, RedirectAttributes redirectAttributes) {
        if (bookDto.getId() != null && bookDto.getId().length() == 0) {
            bookDto.setId(null);
        }
        Book savedBook = bookService.save(bookDto);
        redirectAttributes.addFlashAttribute("message", "Book saved");
        return getRedirectToBook(savedBook.getId());
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes) {
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Book ID " + id + "deleted");
        return "redirect:/books";
    }


    @PostMapping("/{id}/comments/save")
    public String saveComment(@PathVariable(name = "id") String bookId, CommentDto commentDto, RedirectAttributes redirectAttributes) {
        Book book = bookService.findOne(bookId);
        Comment сomment = new Comment(bookId, commentDto.getCommentator(), commentDto.getText(), commentDto.getDate());
        book.addComment(сomment);
        bookService.save(book);

        redirectAttributes.addFlashAttribute("message", "Comment saved");
        return getRedirectToBook(bookId);
    }



    private void addAuthorsAndGenres(Model model) {
        model.addAttribute("allAuthors", authorService.findAll());
        model.addAttribute("allGenres", genreService.findAll());
    }

    private String getRedirectToBook(String bookId) {
        return "redirect:/books/" + bookId;
    }
}
