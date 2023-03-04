package com.demo.enrollment.config;

import ddtrot.com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import ddtrot.com.timgroup.statsd.StatsDClient;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfig  {

    private final ServiceProperties properties;

    public ContextConfig(ServiceProperties properties) {
        this.properties = properties;
    }

    @Bean
    public StatsDClient statsDClient() {
        InstrumentationConfig config = properties.getInstrumentationConfig();
        return new NonBlockingStatsDClientBuilder()
                .prefix(config.getStatsDClientPrefix())
                .hostname(config.getAgentHost())
                .port(config.getAgentPort())
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("enrollments-service")
                .displayName("Enrollments Service")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .contact(new Contact()
                        .email("edouard.zerbo@intellibus.com")
                        .name("Edouard B. Zerbo")
                        .url("https://intellibus.com/people/ezerbo"))
                .description("Service to Enroll Students into Courses (Datadog APM Demo)")
                .version("1.0")
                .title("Enrollments Service");
        return new OpenAPI()
                .info(info);
    }

}
