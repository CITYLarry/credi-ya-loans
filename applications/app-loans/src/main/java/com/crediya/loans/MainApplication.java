package com.crediya.loans;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "CrediYa - Authentication API",
        version = "1.0.0",
        description = "API for loan management in the CrediYa platform."
))
public class MainApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(MainApplication.class, args);
    }
}
