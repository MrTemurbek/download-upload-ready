package com.example.controllers;

import com.example.models.Item;
import com.example.repo.ItemRepository;
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

    private final JWT jwt;

        protected static SecureRandom random = new SecureRandom();

        public synchronized String generateToken( String username ) {
            long longToken = Math.abs( random.nextLong() );
            return (Long.toString( longToken, 16 ));
        }

    ObjectMapper mapper = new ObjectMapper();
    private final ItemRepository itemRepository;

    private final Path basePath = Paths.get("./src/main/resources/upload/");

    public UploadController(JWT jwt, ItemRepository itemRepository) {
        this.jwt = jwt;
        this.itemRepository = itemRepository;
    }

    @PostMapping(value = "file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)

    public Mono<Void> uploadV2(@RequestPart("fileToUpload") Mono<FilePart> filePartMono, Authentication authentication) {
        return filePartMono
                .flatMap(fp -> {
                    String token =jwt.createJWT(authentication.getName(), fp.filename());
                    Item items = mapper.convertValue(new Item(fp.filename(), authentication.getName(), basePath.toString() + fp.filename(), token), Item.class);
                    System.out.println(token);
                    return itemRepository.save(items).then(Mono.just(fp));
                })
                .flatMap(fp -> fp.transferTo(basePath.resolve(fp.filename())))
                .then();
    }

}