package com.greglturnquist.learningspringboot.learningspringboot.rest;

import com.greglturnquist.learningspringboot.learningspringboot.entity.Chapter;
import com.greglturnquist.learningspringboot.learningspringboot.repositories.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChapterController {

    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterController(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @GetMapping("/chapters")
    public Flux<Chapter> listing(){
        return chapterRepository.findAll();
    }
}
