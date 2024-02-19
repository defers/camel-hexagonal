package com.defers.camel.app.config;

import com.example.customerservice.CustomerService;
import com.example.customerservice.ObjectFactory;
import java.util.HashMap;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {
    @Bean
    JaxbDataFormat jaxbDataFormat() {
        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
        jaxbDataFormat.setContextPath(ObjectFactory.class.getPackage().getName());
        return jaxbDataFormat;
    }
    // Исправление ошибки "The request object has been recycled and is no longer associated with this facade"
    // https://github.com/spring-projects/spring-boot/issues/36763
    @Bean
    TomcatConnectorCustomizer disableFacadeDiscard() {
        return connector -> connector.setDiscardFacades(false);
    }

    @Bean
    public CxfEndpoint customers() {
        var cxfEndpoint = new CxfEndpoint();
        cxfEndpoint.setServiceClass(CustomerService.class);
        cxfEndpoint.setWsdlURL("static/server.wsdl");
        cxfEndpoint.setAddress("/customers");
        cxfEndpoint.setProperties(new HashMap<>());
        cxfEndpoint.getProperties().put("schema-validation-enabled", "true");
        cxfEndpoint.setAddress("/ws"); // http://localhost:8080/services/ws/customers?wsdl
        return cxfEndpoint;
    }
}
