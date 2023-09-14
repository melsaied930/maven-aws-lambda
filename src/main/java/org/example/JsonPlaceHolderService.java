package org.example;

import org.example.model.User;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface JsonPlaceHolderService {
    @GetExchange("/users")
    List<User> addUsers();
}
