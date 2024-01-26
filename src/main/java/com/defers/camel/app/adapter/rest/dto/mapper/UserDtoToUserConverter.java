package com.defers.camel.app.adapter.rest.dto.mapper;

import com.defers.camel.app.adapter.rest.dto.UserDto;
import com.defers.camel.domain.user.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {
    private final ModelMapper modelMapper;

    public UserDtoToUserConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public User convert(UserDto source) {
        return modelMapper.map(source, User.class);
    }
}
