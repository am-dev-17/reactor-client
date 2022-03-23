package com.greglturnquist.learningspringboot.learningspringboot.entity;

import com.greglturnquist.learningspringboot.learningspringboot.images.domain.Image;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageTest {

    @Test
    public void imagesManagedByLombokShouldWork(){
        Image image = new Image("id", "file-name.jpg");
        assertThat(image.getId()).isEqualTo("id");
        assertThat(image.getName()).isEqualTo("file-name.jpg");
    }

}