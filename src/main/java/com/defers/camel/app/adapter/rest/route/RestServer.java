package com.defers.camel.app.adapter.rest.route;

import com.defers.camel.app.adapter.rest.dto.UserDto;
import com.defers.camel.domain.user.model.User;
import java.util.List;
import java.util.Objects;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestServer extends RouteBuilder {
    private static final String BASE_PATH = "/users";
    private final ConversionService conversionService;

    public RestServer(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public void configure() throws Exception {
        exceptionHandler();
        getAllUsers();
        createUser();
        updateUser();
        deleteUser();
    }

    private void deleteUser() {
        rest(BASE_PATH)
                .delete("/{id}")
                .description("Delete user")
                .outType(UserDto.class)
                .to("direct:delete-user");

        from("direct:delete-user")
                .routeId("user-delete-direct-route")
                .log("Incoming request body for delete user with id ${header.id}")
                .to("bean:userUseCase?method=deleteById(${header.id})")
                .process(exchange -> {
                    var user = exchange.getMessage().getBody(User.class);
                    exchange.getMessage().setBody(conversionService.convert(user, UserDto.class));
                })
                .log("Deleted user is ${body}");
    }

    private void updateUser() {
        rest(BASE_PATH)
                .put("/{id}")
                .description("Update user")
                .type(UserDto.class)
                .outType(UserDto.class)
                .to("direct:update-user");

        from("direct:update-user")
                .routeId("user-update-direct-route")
                .log("Incoming request body for update user ${body}")
                .process(exchange -> {
                    var user = conversionService.convert(exchange.getMessage().getBody(UserDto.class), User.class);
                    exchange.getMessage().setBody(user);
                })
                .log("Body after transformation is ${body} with headers: ${headers}")
                .to("bean:userUseCase?method=update(${header.id}, ${body})");
    }

    private void exceptionHandler() {
        onException(Exception.class)
                .log(LoggingLevel.ERROR, "Exception catched")
                .bean(ExceptionHandler.class)
                .handled(true);
    }

    private void createUser() {
        rest(BASE_PATH)
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
        rest(BASE_PATH)
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
