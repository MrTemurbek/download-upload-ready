package com.example.service;

import com.example.models.Item;
import com.example.models.Person;
import com.example.repo.ItemRepository;
import com.example.repo.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.security.auth.login.AccountExpiredException;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class PersonService implements ReactiveUserDetailsService {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    private final PeopleRepository peopleRepository;
    private final ItemRepository itemRepository;

    public PersonService(PasswordEncoder passwordEncoder, PeopleRepository peopleRepository, ItemRepository itemRepository) {
        this.passwordEncoder = passwordEncoder;
        this.peopleRepository = peopleRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return  peopleRepository.findByUsername(username)
                .map(person ->{
                    boolean expiration = true;
                    if(person.getExpirationTime() < System.currentTimeMillis()){
                        expiration= false;
                    }
                    return new User(person.getUsername(), person.getPassword(), true, expiration,
                            true, true, List.of(new SimpleGrantedAuthority(person.getRole())));}


//                        new User(person.getUsername(), person.getPassword(), List.of(new SimpleGrantedAuthority(person.getRole())))


                );

    }


    public Mono<Object> addData(String username){
        return peopleRepository.findByUsername(username)
                .flatMap(data->{
                    Item item=new Item();
                    item.setUsername(data.getUsername());
                    return itemRepository.save(item);
                });
    }

    public void registerUser(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setExpirationTime(System.currentTimeMillis() + 120000L);
        peopleRepository.save(person).subscribe();
    }


    private Map<String, Person> data;

}
