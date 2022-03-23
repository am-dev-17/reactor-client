package com.greglturnquist.learningspringboot.learningspringboot.comments.controllers;

import com.greglturnquist.learningspringboot.learningspringboot.comments.domain.Comment;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {

    private final RabbitTemplate rabbitTemplate;
    private final MeterRegistry meterRegistry;

    public CommentController(RabbitTemplate rabbitTemplate, MeterRegistry meterRegistry) {
        this.rabbitTemplate = rabbitTemplate;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        return newComment.flatMap(comment ->
                        Mono.fromRunnable(() ->
                                        rabbitTemplate
                                                .convertAndSend(
                                                        "learning-spring-boot",
                                                        "comments.new",
                                                        comment))
                                .then(Mono.just(comment)))
                .log("commentService-publish")
                .flatMap(comment -> {
                    meterRegistry
                            .counter("comments.produced", "imageId", comment.getImageId())
                            .increment();
                    return Mono.just("redirect:/");
                });

    }

//    A direct exchange routes messages based on a fixed routing key, often the name of the queue. For example, the last code that we just looked at mentioned learning-spring-boot as the name of exchange and comments.new as the routing key. Any consumer that binds their own queue to that exchange with a routing key of comments.new will receive a copy of each message posted earlier.
//
//    A topic exchange allows routing keys to have wildcards like comments.*. This situation best suits clients where the actual routing key isn't known until a user provides the criteria. For example, imagine a stock-trading application where the user must provide a list of ticker symbols he or she is interested in monitoring.
//
//    A fanout exchange blindly broadcasts every message to every queue that is bound to it, regardless of the routing key.
}
