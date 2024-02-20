package com.defers.camel.app.config;

import com.defers.camel.domain.customer.port.in.CustomerUseCase;
import com.defers.camel.domain.customer.usecase.CustomerService;
import com.defers.camel.domain.user.port.in.UserUseCase;
import com.defers.camel.domain.user.port.out.UserRepository;
import com.defers.camel.domain.user.usecase.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "com.defers.camel")
public class AppConfig {
    @Bean
    public UserUseCase userUseCase(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public CustomerUseCase customerUseCase() {
        return new CustomerService();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
