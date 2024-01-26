package com.defers.camel.app.adapter.rest.route;

import com.defers.camel.app.adapter.rest.dto.Response;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ExceptionHandler {
    @Handler
    public void prepareErrorResponse(Exchange exchange) {
        Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);

        Message msg = exchange.getMessage();
        msg.setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        msg.setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.BAD_REQUEST.value());

        Response errorMessage = new Response(cause.getMessage(), "Bad Request");
        msg.setBody(errorMessage, Response.class);
    }
}
