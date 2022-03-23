package com.greglturnquist.learningspringboot.learningspringboot.configuration;

import com.greglturnquist.learningspringboot.learningspringboot.comments.domain.Comment;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public CommandLineRunner setup(MongoOperations mongoOperations){
        return args -> {
            mongoOperations.dropCollection(Comment.class);
        };
    }
}
