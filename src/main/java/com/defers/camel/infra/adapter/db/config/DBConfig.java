package com.defers.camel.infra.adapter.db.config;

import com.defers.camel.domain.user.port.out.UserRepository;
import com.defers.camel.infra.adapter.db.repository.UserInMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

@Configuration
public class DBConfig {
    @Bean
    public UserRepository userRepository(ConversionService conversionService) {
        return new UserInMemoryRepository(conversionService);
    }
}
