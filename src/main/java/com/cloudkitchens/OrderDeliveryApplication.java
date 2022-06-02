package com.cloudkitchens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
@EnableAsync
@ComponentScan(basePackages = {"com.cloudkitchens"})
@Configuration
//@EnableSwagger2
@SpringBootApplication
public class OrderDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderDeliveryApplication.class, args);
    }

}
