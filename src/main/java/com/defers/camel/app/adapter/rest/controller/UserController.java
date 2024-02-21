package com.defers.camel.app.adapter.rest.controller;

import com.defers.camel.domain.user.model.User;
import com.defers.camel.domain.user.port.in.UserUseCase;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/camel-app/users")
@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class.getName());
    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping
    public List<User> getUsers(
            @RequestHeader Map<String, String> headers, @RequestHeader(required = false) String traceparent) {
        log.info("Request to camel app controller getUsers");
        log.info("Trace header: %s".formatted(traceparent));
        return userUseCase.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Request to camel app controller create, with body: %s".formatted(user));
        return userUseCase.create(user);
    }
}
