package com.defers.camel.app.config;

import com.defers.camel.domain.user.port.in.UserUseCase;
import com.defers.camel.domain.user.port.out.UserRepository;
import com.defers.camel.domain.user.usecase.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public UserUseCase userUseCase(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
