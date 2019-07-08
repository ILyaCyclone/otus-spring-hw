package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    public CommentServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Comment> findByBookId(String bookId) {
        return bookRepository.findOne(bookId)
                .block() //TODO unblock
                .getComments();
    }

    @Override
    public void create(CommentDto commentDto) {
        Book book = bookRepository.findOne(commentDto.getBookId()).block() //TODO unblock
                ;
        Comment comment = new Comment(commentDto.getCommentator(), commentDto.getText());

        book.addComment(comment);

        bookRepository.save(book);
    }

    @Override
    public void delete(String bookId, String commentId) {
        Book book = bookRepository.findOne(bookId).block() //TODO unblock
                ;
        book.removeComment(commentId);
        bookRepository.save(book);
    }
}
