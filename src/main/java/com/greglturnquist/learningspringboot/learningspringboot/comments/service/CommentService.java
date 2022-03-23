package com.greglturnquist.learningspringboot.learningspringboot.comments.service;

import com.greglturnquist.learningspringboot.learningspringboot.comments.domain.Comment;
import com.greglturnquist.learningspringboot.learningspringboot.comments.repository.CommentWriterRepository;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private CommentWriterRepository repository;
    private MeterRegistry meterRegistry;

    public CommentService(CommentWriterRepository repository, MeterRegistry registry) {
        this.repository = repository;
        this.meterRegistry = registry;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value= @Queue,
                    exchange = @Exchange(value= "learning-spring-boot"),
                    key="comments.new"
            )
    )
    public void save(Comment newComment){
        repository
                .save(newComment)
                .log("commentService-save")
                .subscribe(
                        comment -> {
                            meterRegistry
                                    .counter("comments.consumed", "imageId", comment.getImageId())
                                    .increment();
                        }
                );
    }
}
