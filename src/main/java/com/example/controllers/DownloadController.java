package com.example.controllers;

import com.example.models.Item;
import com.example.repo.ItemRepository;
import com.example.service.ItemService;
import com.example.service.PersonService;
import com.example.util.JWT;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class DownloadController {
    private final ItemService itemService;

    public DownloadController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/download/file")
    public Mono<Void> downloadFile(@RequestParam("token") String token,
                                   ServerHttpResponse response ) throws IOException {
        return itemService.download(token, response);
    }

}
