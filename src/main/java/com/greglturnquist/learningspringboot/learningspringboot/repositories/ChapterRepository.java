package com.greglturnquist.learningspringboot.learningspringboot.repositories;

import com.greglturnquist.learningspringboot.learningspringboot.entity.Chapter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository extends ReactiveCrudRepository<Chapter, String> {



}
