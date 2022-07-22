package com.example.service;

import com.example.models.Item;
import com.example.models.Person;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Mono;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PersonService implements ReactiveUserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final PeopleRepository peopleRepository;
    ObjectMapper mapper = new ObjectMapper();
    private final Path basePath = Paths.get("./src/main/resources/upload/");
    private final String path = "./src/main/resources/upload/";

    public PersonService(PasswordEncoder passwordEncoder, PeopleRepository peopleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.peopleRepository = peopleRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return peopleRepository.findByUsername(username)
                .map(person -> {
                            boolean expiration = person.getExpirationTime() >= System.currentTimeMillis();
                            return new User(person.getUsername(), person.getPassword(), true, expiration,
                                    true, true, List.of(new SimpleGrantedAuthority(person.getRole())));
                        }
                );

    }

    public void registerUser(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setExpirationTime(System.currentTimeMillis() + 120000L);
        peopleRepository.save(person).subscribe();
    }


}
