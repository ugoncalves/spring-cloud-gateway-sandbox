package com.example.scgsandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringCloudGatewaySandboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewaySandboxApplication.class, args);
    }

}
