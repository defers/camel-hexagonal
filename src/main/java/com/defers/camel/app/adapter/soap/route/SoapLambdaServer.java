package com.defers.camel.app.adapter.soap.route;

import com.defers.camel.domain.customer.port.in.CustomerUseCase;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.LambdaEndpointRouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.cxf.message.MessageContentsList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "features.soap-lambda-route-builder-enabled")
public class SoapLambdaServer {

    private final JaxbDataFormat jaxbDataFormat;
    private final CustomerUseCase customerUseCase;

    public SoapLambdaServer(JaxbDataFormat jaxbDataFormat, CustomerUseCase customerUseCase) {
        this.jaxbDataFormat = jaxbDataFormat;
        this.customerUseCase = customerUseCase;
    }

    @Bean
    public LambdaEndpointRouteBuilder soapConsumer() {
        return rb -> rb.from(rb.cxf("bean:customers"))
                .transform(rb.simple("${body[0]}"))
                .log("header operation name: ${header.operationName}")
                .recipientList(rb.simple("direct:${header.operationName}"));
    }

    @Bean
    public LambdaEndpointRouteBuilder soapHandlerUpdateCustomer() {
        return rb -> rb.from(rb.direct("updateCustomer"))
                .marshal(jaxbDataFormat)
                .log(LoggingLevel.INFO, "updateCustomer body: ${body}")
                .to(rb.jms("queue:camel-update-customer"));
    }

    @Bean
    public LambdaEndpointRouteBuilder soapHandlerGetCustomersByName() {
        return rb -> rb.from(rb.direct("getCustomersByName"))
                .bean(customerUseCase, "find")
                .process(exchange -> {
                    var resultList = new MessageContentsList();
                    resultList.add(exchange.getMessage().getBody());
                    exchange.getMessage().setBody(resultList);
                });
    }
}
