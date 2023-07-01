package com.demo.grade.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class ServiceProperties {

    private InstrumentationConfig instrumentationConfig = new InstrumentationConfig();

    private ExternalServiceConfig enrollmentsServiceConfig = new ExternalServiceConfig();

    private ExternalServiceConfig tuitionServiceConfig = new ExternalServiceConfig();
}
