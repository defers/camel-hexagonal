package com.defers.camel.infra.adapter.db.repository;

import com.defers.camel.domain.user.model.User;
import com.defers.camel.domain.user.port.out.UserRepository;
import com.defers.camel.infra.adapter.db.exception.ElementNotFoundException;
import com.defers.camel.infra.adapter.db.model.UserModel;
import java.util.*;
import org.springframework.core.convert.ConversionService;

public class UserInMemoryRepository implements UserRepository {
    private final Map<Integer, UserModel> users = new HashMap<>();
    private final ConversionService conversionService;

    public UserInMemoryRepository(ConversionService conversionService) {
        this.conversionService = conversionService;

        UserModel admin = new UserModel(1, "Admin");
        UserModel user = new UserModel(2, "User");
        users.put(admin.getId(), admin);
        users.put(user.getId(), user);
    }

    @Override
    public List<User> findAll() {
        return users.values().stream()
                .map(userModel -> conversionService.convert(userModel, User.class))
                .toList();
    }

    @Override
    public User create(User user) {
        users.put(user.getId(), conversionService.convert(user, UserModel.class));
        return user;
    }

    @Override
    public User update(User user) {
        var id = user.getId();
        findById(id);
        var userModel = users.get(id);
        userModel.setName(user.getName());
        return user;
    }

    @Override
    public User findById(int id) {
        return Optional.ofNullable(users.get(id))
                .map(u -> conversionService.convert(u, User.class))
                .orElseThrow(() -> new ElementNotFoundException("User with id %s not found".formatted(id)));
    }

    @Override
    public User deleteById(int id) {
        findById(id);
        return conversionService.convert(users.remove(id), User.class);
    }
}
