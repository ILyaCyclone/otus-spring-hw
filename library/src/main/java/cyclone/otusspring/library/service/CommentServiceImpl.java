package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Comment> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public void create(CommentDto commentDto) {
        Book book = bookRepository.findOne(commentDto.getBookId());
        Comment comment = new Comment(commentDto.getCommentator(), commentDto.getText(), book);

        book.addComment(comment);

        bookRepository.save(book);
    }

    @Override
    public void delete(String commentId) {
        commentRepository.delete(commentId);
    }
}
