package com.example.controllers;

import com.example.models.Item;
import com.example.repo.ItemRepository;
import com.example.service.ItemService;
import com.example.service.PersonService;
import com.example.util.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.SecureRandom;

@RestController
@RequestMapping("upload")
public class UploadController {
        private final ItemService itemService;
    public UploadController(ItemService itemService) {
        this.itemService = itemService;
    }
    @PostMapping(value = "file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> uploadV2(@RequestPart("fileToUpload") Mono<FilePart> filePartMono, Authentication authentication) {
        return itemService.upload(filePartMono, authentication);
    }

}