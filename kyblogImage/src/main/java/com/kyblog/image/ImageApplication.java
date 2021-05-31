package com.kyblog.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: kyblog
 * @description:
 * @author: Kim_yuan
 * @create: 2021-05-10 16:48
 **/
@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com.kyblog.api")
public class ImageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageApplication.class, args);
    }
}
