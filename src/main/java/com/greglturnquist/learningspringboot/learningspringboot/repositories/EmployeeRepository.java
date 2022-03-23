package com.greglturnquist.learningspringboot.learningspringboot.repositories;

import com.greglturnquist.learningspringboot.learningspringboot.entity.Employee;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String>, ReactiveQueryByExampleExecutor<Employee> {

}
