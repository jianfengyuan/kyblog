package com.kyblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class eurekaServer_8003 {
    public static void main(String[] args) {
        SpringApplication.run(eurekaServer_8003.class, args);
    }
}
