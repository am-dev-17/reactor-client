package com.greglturnquist.learningspringboot.learningspringboot.images.services;

import com.greglturnquist.learningspringboot.learningspringboot.images.domain.Image;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class BlockingImageService {

    private final ImageService imageService;

    public BlockingImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public List<Image> findAllImages() {
        return imageService.findAllImages()
                .collectList()
                .block(Duration.ofSeconds(10));
    }

    public Resource findOneImage(String filename) {
        return imageService.findOneImage(filename)
                .block(Duration.ofSeconds(30));
    }

    public void createImage(List<FilePart> files) {
        imageService.createImage(Flux.fromIterable(files))
                .block(Duration.ofMinutes(1));
    }

}
