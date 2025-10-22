package com.infnet.enrollmentmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class EnrollmentModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrollmentModuleApplication.class, args);
    }

}
