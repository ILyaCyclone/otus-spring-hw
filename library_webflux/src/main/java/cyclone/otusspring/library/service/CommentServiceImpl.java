package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Comment;
import cyclone.otusspring.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    public CommentServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Flux<Comment> findByBookId(String bookId) {
        return bookRepository.findOne(bookId)
                .map(Book::getComments)
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Void> create(CommentDto commentDto) {
        // find a book by commentDto.bookId; create a Comment from CommentDto; add comment to book; save book
//
//        return bookRepository.findOne(commentDto.getBookId())
////                .doOnEach(bookSignal -> bookSignal.get().addComment(new Comment(commentDto.getCommentator(), commentDto.getText())))
//                .map(book -> {book.addComment(new Comment(commentDto.getCommentator(), commentDto.getText())); return book;})
//                .map(bookRepository::save)
//                .then();

        Book book = bookRepository.findOne(commentDto.getBookId()).block() //TODO unblock
                ;
        Comment comment = new Comment(commentDto.getCommentator(), commentDto.getText());

        book.addComment(comment);

        return bookRepository.save(book)
                .then();
    }

    @Override
    public Mono<Void> delete(String bookId, String commentId) {
        // find book by id; remove comment from found book; save found book
        Book book = bookRepository.findOne(bookId).block();
        book.removeComment(commentId);
        return bookRepository.save(book)
                .then();

//        return bookRepository.findOne(bookId)
//                .map(book -> {book.removeComment(commentId); return book;})
////                .doOnEach(bookSignal -> bookSignal.get().removeComment(commentId))
//                .map(bookRepository::save)
//                .then();

//        return bookRepository.findOne(bookId)
//                .flatMap(boo -> {boo.removeComment(commentId); return Mono.just(boo);})
//                .flatMap(bookRepository::save)
//                .then();
    }
}
