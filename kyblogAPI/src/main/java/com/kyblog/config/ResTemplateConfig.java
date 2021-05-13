package com.kyblog.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-08 17:03
 **/
@Configuration
public class ResTemplateConfig {
    @Bean(value = "restTemplateWithRibbon")
    @LoadBalanced
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

    @Bean(value = "restTemplateNoRibbon")
    public RestTemplate createRestTemplateNoRibbon() {
        return new RestTemplate();
    }
}
