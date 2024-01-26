package com.defers.camel.app.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //        from("timer:timer-main?period=3000")
        //                .log("Hello " + LocalDateTime.now());
    }
}
