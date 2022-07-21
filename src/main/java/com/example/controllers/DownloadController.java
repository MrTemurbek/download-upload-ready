package com.example.controllers;

import com.example.repo.ItemRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@RestController
public class DownloadController {
    private final ItemRepository itemRepository;

    public DownloadController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/download/file")
    public Mono<Void> downloadFile(@RequestParam("file-name") String token,
                                   ServerHttpResponse response) throws IOException {
        return getFileName(token).flatMap(file1 -> {
            try {
                ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
                response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + file1 + "");
                response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
                ClassPathResource resource = new ClassPathResource("upload/" + file1);
                File file = resource.getFile();
                return zeroCopyResponse.writeWith(file, 0, file.length());
            } catch (Exception e) {
                return Mono.error(new RuntimeException(e));
            }
        });
    }

    Mono<String> getFileName(String token) throws RuntimeException {
        return itemRepository.findAllByToken(token)
                .flatMap(data -> {
                    return Mono.just(data.getName());
                });
    }
}
