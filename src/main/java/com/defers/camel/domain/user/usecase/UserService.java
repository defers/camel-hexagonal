package com.defers.camel.domain.user.usecase;

import com.defers.camel.domain.user.model.User;
import com.defers.camel.domain.user.port.in.UserUseCase;
import com.defers.camel.domain.user.port.out.UserRepository;
import java.util.List;

public class UserService implements UserUseCase {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public User update(int id, User user) {
        return userRepository.update(user);
    }

    @Override
    public User deleteById(int id) {
        userRepository.findById(id);
        return userRepository.deleteById(id);
    }
}