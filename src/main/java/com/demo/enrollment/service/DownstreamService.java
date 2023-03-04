package com.demo.enrollment.service;

import com.demo.enrollment.instrumentation.Status;
import datadog.trace.api.Trace;
import ddtrot.com.timgroup.statsd.StatsDClient;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DownstreamService {

    private final StatsDClient statsDClient;

    public DownstreamService(StatsDClient statsDClient) {
        this.statsDClient = statsDClient;
    }

    @Trace(operationName = "downstream.call")
    public void callEndpoint(String url) {
        Span span = GlobalTracer.get().activeSpan();
        span.setTag("downstream.url", url);
        span.log("Logging something here!");
        RestTemplate template = new RestTemplate();
        template.getForEntity(url, Object.class);
        statsDClient.serviceCheck(Status.ok(new String[]{"service:".concat(System.getProperty("dd.service"))}));
    }

    public void exception() {
        statsDClient.serviceCheck(Status.critical(new String[]{"service:".concat(System.getProperty("dd.service"))}));
        throw new RuntimeException("An exception was thrown here");
    }

}
