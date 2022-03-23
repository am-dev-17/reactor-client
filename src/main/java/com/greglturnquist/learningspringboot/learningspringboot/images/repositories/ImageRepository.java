package com.greglturnquist.learningspringboot.learningspringboot.images.repositories;

import com.greglturnquist.learningspringboot.learningspringboot.images.domain.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, String> {
     Mono<Image> findByName(String name);
}
