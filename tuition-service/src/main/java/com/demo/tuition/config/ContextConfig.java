package com.demo.tuition.config;

import ddtrot.com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import ddtrot.com.timgroup.statsd.StatsDClient;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ContextConfig implements WebMvcConfigurer {

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
                .group("tuition-service")
                .displayName("Tuition Service")
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
                .description("Service to Track Tuition Payments (Datadog APM Demo)")
                .version("1.0")
                .title("Tuition Service");
        return new OpenAPI()
                .info(info);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE", "HEAD");
    }
}
