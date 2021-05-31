package com.kyblog.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-10 09:18
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kyblog.api", "com.kyblog.comment"})
public class CommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentApplication.class, args);
    }
}
