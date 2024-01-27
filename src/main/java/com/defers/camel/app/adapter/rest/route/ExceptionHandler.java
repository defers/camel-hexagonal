package com.defers.camel.app.adapter.rest.route;

import com.defers.camel.app.adapter.rest.dto.Response;
import com.defers.camel.domain.exception.EntityValidationException;
import com.defers.camel.infra.adapter.db.exception.ElementNotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @Handler
    public void prepareErrorResponse(Exchange exchange) {
        Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
        log.error(cause.getMessage(), cause);

        var responseStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        var error = "Internal error";

        if (cause instanceof EntityValidationException || cause instanceof ElementNotFoundException) {
            responseStatus = HttpStatus.BAD_REQUEST.value();
            error = "Bad Request";
        }

        Message msg = exchange.getMessage();
        msg.setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        msg.setHeader(Exchange.HTTP_RESPONSE_CODE, responseStatus);

        Response errorMessage = new Response(cause.getMessage(), error);
        msg.setBody(errorMessage, Response.class);
    }
}
