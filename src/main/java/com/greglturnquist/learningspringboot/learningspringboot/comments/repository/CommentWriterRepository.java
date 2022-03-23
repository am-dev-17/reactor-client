package com.greglturnquist.learningspringboot.learningspringboot.comments.repository;

import com.greglturnquist.learningspringboot.learningspringboot.comments.domain.Comment;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentWriterRepository extends Repository<Comment, String> {

    Mono<Comment>  save(Comment comment);

    Mono<Comment> findById(String id);
}
