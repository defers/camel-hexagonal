package com.defers.camel.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jms")
public record JmsProperties(Redelivery redelivery) {
    public record Redelivery(
            Long initialRedeliveryDelay,
            Double backOffMultiplier,
            Integer maximumRedeliveries,
            Integer maximumRedeliveryDelay) {}
}
