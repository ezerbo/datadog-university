package com.demo.enrollment.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentationConfig {

    private String agentHost;

    private int agentPort;

    private String statsDClientPrefix;


}
