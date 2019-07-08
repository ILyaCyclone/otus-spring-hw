package cyclone.otusspring.library.service;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {

    Flux<Comment> findByBookId(String bookId);

    Mono<Void> create(CommentDto commentDto);

    Mono<Void> delete(String bookId, String commentId);
}
