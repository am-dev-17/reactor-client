package com.greglturnquist.learningspringboot.learningspringboot.images.controllers;

import com.greglturnquist.learningspringboot.learningspringboot.images.domain.Image;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class ImageController {
    private final String API_BASE_PATH = "api";

    @GetMapping(API_BASE_PATH + "/images")
    Flux<Image> images(){
        return Flux.just(
                new Image("1", "learning-spring-boot-cover.jpg"),
                new Image("2", "learning-spring-boot-2nd-edition-cover.jpg"),
                new Image("3", "bazinga.png")
        );
    }

    @PostMapping(API_BASE_PATH + "/images")
    Mono<Void> create(@RequestBody Flux<Image> images){
        return images
                .map(image -> {
                    return image;
                })
                .then();
    }
}
