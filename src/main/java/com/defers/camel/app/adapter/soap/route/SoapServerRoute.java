package com.defers.camel.app.adapter.soap.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "features.soap-route-builder-enabled")
public class SoapServerRoute extends RouteBuilder {

    private final JaxbDataFormat jaxbDataFormat;

    public SoapServerRoute(JaxbDataFormat jaxbDataFormat) {
        this.jaxbDataFormat = jaxbDataFormat;
    }

    @Override
    public void configure() {
        from("cxf:bean:customers")
                // Сообщение приходит с типом MessageContentsList, в котором нулевой элемент
                // это наш сгенерированный POJO класс
                .transform(simple("${body[0]}"))
                .log("header operation name: ${header.operationName}")
                .recipientList(simple("direct:${header.operationName}"));

        from("direct:updateCustomer")
                .process(e -> log.info("direct:updateCustomer"))
                .marshal(jaxbDataFormat)
                .log(LoggingLevel.INFO, "updateCustomer body: ${body}")
                .to("jms:queue:camel-update-customer");
    }
}
