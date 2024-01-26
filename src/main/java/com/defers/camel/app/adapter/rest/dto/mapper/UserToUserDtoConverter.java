package com.defers.camel.app.adapter.rest.dto.mapper;

import com.defers.camel.app.adapter.rest.dto.UserDto;
import com.defers.camel.domain.user.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {
    private final ModelMapper modelMapper;

    public UserToUserDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto convert(User source) {
        return modelMapper.map(source, UserDto.class);
    }
}
