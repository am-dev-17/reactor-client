package com.greglturnquist.learningspringboot.learningspringboot.comments.repository;

import com.greglturnquist.learningspringboot.learningspringboot.comments.domain.Comment;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;

public interface CommentReaderRepository extends Repository<Comment, String> {

    Flux<Comment> findByImageId(String imageId);
}
