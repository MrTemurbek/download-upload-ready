package com.example.repo;

import com.example.models.Item;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ItemRepository extends R2dbcRepository<Item, Long> {

    @Query("SELECT i.path FROM items i where i.token =:token")
    Mono<String> getFileNameByToken(String token);

    Mono<Item> findAllByToken(String token);
}
