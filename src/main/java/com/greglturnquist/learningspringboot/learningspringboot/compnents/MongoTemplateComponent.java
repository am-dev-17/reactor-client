package com.greglturnquist.learningspringboot.learningspringboot.compnents;

import com.greglturnquist.learningspringboot.learningspringboot.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class MongoTemplateComponent {

    private final ReactiveMongoOperations operations;

    @Autowired
    public MongoTemplateComponent(ReactiveMongoOperations operations) {
        this.operations = operations;
    }

    public Mono<Void> runMongoTemplateExample(){
        Employee employee = new Employee();
        employee.setFirstName("Bilbo");
        Example<Employee> example = Example.of(employee);

        Mono<Employee> singleEmployee = operations.findOne(
                new Query(byExample(example)), Employee.class);
        return null;
    }

    public void customCriteriaQuery(){
        Employee e = new Employee();
        e.setLastName("baggins"); // Lowercase lastName

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withMatcher("lastName", startsWith())
                .withIncludeNullValues();

        Example<Employee> example = Example.of(e, matcher);

        Flux<Employee> multipleEmployees = operations.find(
                new Query(byExample(example)), Employee.class);

        operations
                .findOne(
                        query(
                                where("firstName").is("Frodo")), Employee.class);
    }
}
