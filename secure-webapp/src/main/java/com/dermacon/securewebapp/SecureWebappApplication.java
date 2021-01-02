package com.dermacon.securewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.log4j.Logger;

@SpringBootApplication
public class SecureWebappApplication {


    static Logger log = Logger.getLogger(SecureWebappApplication.class.getName());

    public static void main(String[] args) {
//        SpringApplication.run(SecureWebappApplication.class, args);
        log.trace("Trace Message!");
        log.debug("Debug Message!");
        log.info("Info Message!");
        log.warn("Warn Message!");
        log.error("Error Message!");
        log.fatal("Fatal Message!");

    }

}
