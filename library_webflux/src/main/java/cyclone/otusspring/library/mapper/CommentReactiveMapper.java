package cyclone.otusspring.library.mapper;

import cyclone.otusspring.library.dto.CommentDto;
import cyclone.otusspring.library.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CommentReactiveMapper {

    private final CommentMapper commentMapper;

    public Mono<Comment> toComment(Mono<CommentDto> commentDtoMono) {
        return commentDtoMono.map(commentMapper::toComment);
    }

    public Mono<CommentDto> toCommentDto(Mono<Comment> commentMono, String bookId) {
        return commentMono.map(comment -> commentMapper.toCommentDto(comment, bookId));
    }

    public Flux<Comment> toCommentFlux(Flux<CommentDto> commentDtoFlux) {
        return commentDtoFlux.map(commentMapper::toComment);
    }

    public Flux<CommentDto> toCommentDtoFlux(Flux<Comment> commentFlux, String bookId) {
        return commentFlux.map(comment -> commentMapper.toCommentDto(comment, bookId));
    }

}
