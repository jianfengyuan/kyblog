package com.kyblog.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-08 16:27
 **/

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.kyblog.api","com.kyblog.front"})
public class FrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class, args);
    }
}
