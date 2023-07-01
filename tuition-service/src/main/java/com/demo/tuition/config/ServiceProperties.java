package com.demo.tuition.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "app")
public class ServiceProperties {

    private InstrumentationConfig instrumentationConfig = new InstrumentationConfig();

}
