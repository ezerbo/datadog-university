package com.demo.enrollment.instrumentation;

import ddtrot.com.timgroup.statsd.ServiceCheck;

public class Status {

    private final static String SERVICE_CHECK_NAME = "service.status";

    public static ServiceCheck ok(String[] tags) {
        return check(ServiceCheck.Status.OK, tags);
    }

    public static ServiceCheck critical(String[] tags) {
        return check(ServiceCheck.Status.CRITICAL, tags);
    }

    private static ServiceCheck check(ServiceCheck.Status status, String[] tags) {
        return ServiceCheck.builder()
                .withName(SERVICE_CHECK_NAME)
                .withStatus(status)
                .withTags(tags)
                .withMessage("Check Message")
                .build();
    }

}
