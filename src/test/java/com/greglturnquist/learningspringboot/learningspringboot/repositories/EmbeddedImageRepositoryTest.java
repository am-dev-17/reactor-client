package com.greglturnquist.learningspringboot.learningspringboot.repositories;

import com.greglturnquist.learningspringboot.learningspringboot.images.domain.Image;
import com.greglturnquist.learningspringboot.learningspringboot.images.repositories.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
class EmbeddedImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private MongoOperations operations;

    @Before
    public void setup(){
        operations.dropCollection(Image.class);
        operations.insert(new Image("1",
                "learning-spring-boot-cover.jpg"));
        operations.insert(new Image("2",
                "learning-spring-boot-2nd-edition-cover.jpg"));
        operations.insert(new Image("3",
                "bazinga.png"));
        operations.findAll(Image.class).forEach(image -> {
            System.out.println(image.toString());
        });
    }

    @Test
    public void findAllShouldWork(){
        Flux<Image> images = imageRepository.findAll();
        StepVerifier.create(images)
                .recordWith(ArrayList::new)
                .expectNextCount(3)
                .consumeRecordedWith(
                        results -> {
                            assertThat(results).hasSize(3);
                            assertThat(results)
                                    .extracting(Image::getName)
                                    .contains(
                                            "learning-spring-boot-cover.jpg",
                                            "learning-spring-boot-2nd-edition-cover.jpg",
                                            "bazinga.png"
                                    );
                        }
                )
                .expectComplete()
                .verify();
    }


    @Test
    public void findByNameShouldWork(){
        Mono<Image> image = imageRepository.findByName("bazinga.png");
        StepVerifier.create(image)
                .expectNextMatches(
                        results -> {
                            assertThat(results.getName()).isEqualTo("bazinga.png");
                            assertThat(results.getId()).isEqualTo("3");
                            return true;
                        }
                );
    }

}