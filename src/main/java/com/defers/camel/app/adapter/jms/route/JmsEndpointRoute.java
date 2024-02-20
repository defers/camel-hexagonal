package com.defers.camel.app.adapter.jms.route;

import jakarta.xml.bind.JAXBElement;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.springframework.stereotype.Component;

@Component
public class JmsEndpointRoute extends EndpointRouteBuilder {
    private final JaxbDataFormat jaxbDataFormat;

    public JmsEndpointRoute(JaxbDataFormat jaxbDataFormat) {
        this.jaxbDataFormat = jaxbDataFormat;
    }

    @Override
    public void configure() {
        // Срабатывает для определенного типа Exception
        // Позволяет отправить в DLQ только сообщения, при которых возник данный тип исключения
        onException(IllegalArgumentException.class)
                .handled(true)
                .useOriginalBody()
                .process(exchange -> {
                    Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                    log.error("Error handler onException: %s".formatted(cause.getMessage()), cause);
                })
                .to(jms("queue:camel-update-customer-dlq"));

        // Срабатывает для всех типов Exception
        // Позволяет настраивать политику повторов
        errorHandler(deadLetterChannel("jms:queue:camel-update-customer-dlq")
                .onExceptionOccurred(exchange -> {
                    Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                    log.info("Redelivery number: %s"
                            .formatted(exchange.getMessage().getHeader("CamelRedeliveryCounter")));
                    log.error("Error handler deadLetterChannel: %s".formatted(cause.getMessage()), cause);
                })
                .useOriginalBody()
                .maximumRedeliveries(-1)
                .redeliveryDelay(10_000));

        from(jms("queue:camel-update-customer")
                        .concurrentConsumers(2)
                        .maxConcurrentConsumers(5)
                        .asyncConsumer(true)
                        .transacted(true))
                .log(LoggingLevel.INFO, "Consume message from queue: ${body}")
                .throttle(5)
                .asyncDelayed()
                .unmarshal(jaxbDataFormat)
                .process(exchange -> exchange.getMessage()
                        .setBody(
                                exchange.getMessage().getBody(JAXBElement.class).getValue()))
                .process(exchange -> Thread.sleep(5000))
                .process(exchange -> {
                    throw new IllegalArgumentException("Тестовая ошибка");
                });
    }
}
