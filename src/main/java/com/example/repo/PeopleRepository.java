package com.example.repo;


import com.example.models.Person;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface PeopleRepository extends R2dbcRepository<Person, Integer> {
    Mono<Person> findByUsername(String username);


}
