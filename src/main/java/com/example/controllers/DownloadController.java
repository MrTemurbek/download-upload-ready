package com.example.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

@RestController
public class DownloadController {

    @GetMapping("/download/file")
    public Mono<Void> downloadFile(@RequestParam("file-name") String fileName,
                                   ServerHttpResponse response ) throws IOException {

        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
//        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=" + fileName + "");
//        response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ClassPathResource resource = new ClassPathResource("upload/"+ fileName);
        File file = resource.getFile();
        return zeroCopyResponse.writeWith(file, 0, file.length());
    }
}
