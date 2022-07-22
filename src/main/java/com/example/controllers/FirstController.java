package com.example.controllers;

import com.example.models.Person;
import com.example.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import java.net.URI;


@Controller
public class FirstController {
    private final PersonService personService;

    public FirstController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public String redirectPage() {
        return "redirect";
    }

    @GetMapping("/upload-page")
    public String uploadPage() {
        return "upload";
    }

    @GetMapping("/download-page")
    public String downloadPage() {
        return "download";
    }

    @PostMapping("/registration")
    public String createPerson(@RequestBody Person person) {
        personService.registerUser(person);
        return "redirect:/login";
    }
}



