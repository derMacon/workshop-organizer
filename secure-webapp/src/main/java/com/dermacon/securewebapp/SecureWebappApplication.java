package com.dermacon.securewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@SpringBootApplication
public class SecureWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureWebappApplication.class, args);
    }

}
