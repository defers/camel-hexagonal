package com.defers.camel.app.route;

import com.defers.camel.domain.user.model.User;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestServer extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        rest("/users")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get()
                .description("Find all users")
                .outType(User[].class)
                .responseMessage()
                .code(200)
                .endResponseMessage()
                .to("bean:userUseCase?method=findAll");
    }
}
