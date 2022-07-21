package com.example.controllers;

import com.example.repo.ItemRepository;
import com.example.util.JWT;
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

@RestController
public class DownloadController {
    private final JWT jwt;

    private final ItemRepository itemRepository;

    public DownloadController(JWT jwt, ItemRepository itemRepository) {
        this.jwt = jwt;
        this.itemRepository = itemRepository;
    }

    @GetMapping("/download/file")
    public Mono<Void> downloadFile(@RequestParam("file-name") String token,
                                   ServerHttpResponse response ) throws IOException {
        String fileName =getFileName(token);
        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName + "");
        response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        File file = ResourceUtils.getFile("C:/Users/user/Desktop/download-upload-master/src/main/resources/upload/"+ fileName);
        return zeroCopyResponse.writeWith(file, 0, file.length());
    }


    String getFileName(String token) throws RuntimeException {
        return jwt.decodeJWT(token).getSubject();
    }
}
