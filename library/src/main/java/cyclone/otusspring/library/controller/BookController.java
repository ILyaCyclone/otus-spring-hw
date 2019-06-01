package cyclone.otusspring.library.controller;

import cyclone.otusspring.library.dto.BookDto;
import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.dto.Message;
import cyclone.otusspring.library.mapper.BookMapper;
import cyclone.otusspring.library.mapper.CommentMapper;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.service.AuthorService;
import cyclone.otusspring.library.service.BookService;
import cyclone.otusspring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/books")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookMapper bookMapper;
    private final CommentMapper commentMapper;

    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception ex, RedirectAttributes redirectAttributes) {
        logger.error("Request: " + req.getRequestURL() + " raised " + ex);

        redirectAttributes.addFlashAttribute("message", new Message(ex.getMessage(), Message.Type.ERROR));
        return getRedirectToBooks();
    }



    @GetMapping
    public String books(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books";
    }


    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("bookDto", new BookDto());
        addAuthorsAndGenresToModel(model);
        return "book-form";
    }

    @GetMapping("/{id}")
    public String edit(Model model, @PathVariable(name = "id") String id) {
        Book book = bookService.findOne(id);
        model.addAttribute("bookDto", bookMapper.toDto(book));

        addAuthorsAndGenresToModel(model);
        return "book-form";
    }

    /**
     * Doesn't use сommentDtoList from bookDto.
     * To save comments use {@code BookController::saveComment}.
     *
     * @return
     */
    @PostMapping("/save")
    public String save(BookDto bookDto, RedirectAttributes redirectAttributes) {
        if (bookDto.getId() != null && bookDto.getId().length() == 0) {
            bookDto.setId(null);
        }
        if (bookDto.getId() != null) {
            bookDto.setCommentDtoList(bookService.findOne(bookDto.getId()).getComments()
                    .stream().map(comment -> commentMapper.toCommentDto(comment, bookDto.getId()))
                    .collect(Collectors.toList()));
        }
        Book savedBook = bookService.save(bookDto);
        redirectAttributes.addFlashAttribute("message", new Message("Book saved"));
        return getRedirectToBook(savedBook.getId());
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes) {
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("message", new Message("Book ID " + id + " deleted"));
        return getRedirectToBooks();
    }


    @PostMapping("/{id}/comments/save")
    public String saveComment(@PathVariable(name = "id") String bookId, CommentDto commentDto, RedirectAttributes redirectAttributes) {
        Book book = bookService.findOne(bookId);
        Comment сomment = new Comment(bookId, commentDto.getCommentator(), commentDto.getText(), commentDto.getDate());
        book.addComment(сomment);
        bookService.save(book);

        redirectAttributes.addFlashAttribute("message", new Message("Comment saved"));
        return getRedirectToBook(bookId);
    }



    private void addAuthorsAndGenresToModel(Model model) {
        model.addAttribute("allAuthors", authorService.findAll());
        model.addAttribute("allGenres", genreService.findAll());
    }

    private String getRedirectToBook(String bookId) {
        return "redirect:/books/" + bookId;
    }

    private String getRedirectToBooks() {
        return "redirect:/books";
    }
}
