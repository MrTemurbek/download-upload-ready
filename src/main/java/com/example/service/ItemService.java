package com.example.service;

import com.example.models.Item;
import com.example.repo.ItemRepository;
import com.example.repo.PeopleRepository;
import com.example.util.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ItemService {
    private final JWT jwt;

    private final ItemRepository itemRepository;
    ObjectMapper mapper = new ObjectMapper();
    private final Path basePath = Paths.get("./src/main/resources/upload/");
    private final String path = "./src/main/resources/upload/";

    public ItemService( JWT jwt, ItemRepository itemRepository) {
        this.jwt = jwt;
        this.itemRepository = itemRepository;
    }


    public Mono<Void> download(String token, ServerHttpResponse response) throws FileNotFoundException {
        String fileName =getFileName(token);
        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName + "");
        response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        File file = ResourceUtils.getFile(path + fileName);
        return zeroCopyResponse.writeWith(file, 0, file.length());
    }

    String getFileName(String token) throws RuntimeException {
        return jwt.decodeJWT(token).getSubject();
    }

    public Mono<Void> upload(Mono<FilePart> filePartMono, Authentication authentication){
        return filePartMono
                .flatMap(fp -> {
                    String token =jwt.createJWT(authentication.getName(), fp.filename());
                    Item items = mapper.convertValue(new Item(fp.filename(), authentication.getName(), basePath + fp.filename(), token), Item.class);
                    return itemRepository.save(items).then(Mono.just(fp));
                })
                .flatMap(fp -> fp.transferTo(basePath.resolve(fp.filename())))
                .then();
    }
}
