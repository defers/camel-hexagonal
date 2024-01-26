package com.defers.camel.app.adapter.rest.route;

import com.defers.camel.app.adapter.rest.dto.UserDto;
import com.defers.camel.domain.exception.EntityValidationException;
import com.defers.camel.domain.user.model.User;
import java.util.List;
import java.util.Objects;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestServer extends RouteBuilder {

    private final ConversionService conversionService;

    public RestServer(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public void configure() throws Exception {
        exceptionHandler();
        getAllUsers();
        createUser();
    }

    private void exceptionHandler() {
        onException(EntityValidationException.class)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.BAD_REQUEST))
                .log("Validation exception")
                .bean(ExceptionHandler.class)
                .handled(true);
    }

    private void createUser() {
        rest("/users")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .post()
                .description("Create new user")
                .type(UserDto.class)
                .outType(UserDto.class)
                .to("direct:user-create");

        from("direct:user-create")
                .routeId("user-create-direct-route")
                .log("Incoming Body for create new user is ${body}")
                .process(e -> {
                    var userDto = e.getMessage().getBody(UserDto.class);
                    e.getMessage().setBody(conversionService.convert(userDto, User.class));
                })
                .log("Body after transformation is ${body} with headers: ${headers}")
                .to("bean:userUseCase?method=create(${body})")
                .log("Created User ${body}")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.OK.value()));
    }

    private void getAllUsers() {
        rest("/users")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get()
                .description("Find all users")
                .outType(UserDto[].class)
                .to("direct:get-all-users");

        from("direct:get-all-users")
                .routeId("get-all-users-direct-route")
                .log("Getting all users")
                .to("bean:userUseCase?method=findAll")
                .process(exchange -> {
                    List<User> msg = exchange.getMessage().getBody(List.class);
                    List<UserDto> newMsg = msg.stream()
                            .map(u -> Objects.requireNonNull(conversionService.convert(u, UserDto.class)))
                            .toList();
                    exchange.getMessage().setBody(newMsg);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.OK.value()))
                .log("Headers ${headers}")
                .log("Found users ${body}");
    }
}
