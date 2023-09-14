package org.example.controller;

import jakarta.annotation.PostConstruct;
import org.example.JsonPlaceHolderService;
import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final JsonPlaceHolderService jsonPlaceHolderService;
    List<User> users = new ArrayList<>();

    public UserController(JsonPlaceHolderService jsonPlaceHolderService) {
        this.jsonPlaceHolderService = jsonPlaceHolderService;
    }

    @PostConstruct
    private void init() {
        if (users.isEmpty()) {
            log.info("Adding users from json placeholder");
            users = jsonPlaceHolderService.addUsers();
        }
    }

    //    curl 'http://localhost:8080/users' -H 'Content-Type: application/json' -d '{}'
    @PostMapping
    public User save(@RequestBody User user) {
        User temp = new User(UUID.randomUUID().toString(), user.name(), user.username(), user.email(), user.address());
        users.add(temp);
        return temp;
    }

    //    curl http://localhost:8080/users
    @GetMapping
    public List<User> findAll() {
        return users;
    }

    //    curl http://localhost:8080/users/1
    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable String id) {
        return Optional.ofNullable(users.stream().filter(user -> user.id().equals(id)).findFirst().orElseThrow(() -> new UserNotFoundException("User id=" + id + " not found!")));
    }

    //curl -X PUT 'http://localhost:8080/users/1' -H 'Content-Type: application/json' -d '{"id": "1"}'
    @PutMapping("/{id}")
    public Optional<User> update(@RequestBody User user, @PathVariable String id) {
        //users.stream().filter(u -> u.id().equals(id)).findFirst().ifPresent(value -> users.set(users.indexOf(value), user));
        return Optional.ofNullable(users.stream().filter(u -> u.id().equals(id)).findFirst().map(u -> {
            users.set(users.indexOf(u), user);
            return user;
        }).orElseThrow(() -> new UserNotFoundException("User id=" + id + " not found!")));
    }

    //curl -X DELETE http://localhost:8080/users/1
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        //users.removeIf(u -> u.id().equals(id));
        users.stream().filter(u -> u.id().equals(id)).findFirst().map(u -> users.remove(users.indexOf(u))).orElseThrow(() -> new UserNotFoundException("User id=" + id + " not found!"));
    }
}
