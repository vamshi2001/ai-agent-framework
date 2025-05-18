package com.api.hub.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * Entry point for the AI-Agent Framework application.
 * 
 * <p>This class bootstraps the Spring Boot application context and
 * starts the AI-Agent framework services.</p>
 * 
 * <p>On startup, it initializes all configured components, such as
 * cache handlers, environment state management, and AI agent orchestration.</p>
 * 
 * <p>Logging is enabled to confirm the successful launch of the application.</p>
 * 
 * @author API Hub Team
 */
@SpringBootApplication
@Slf4j
public class ApiHub {

    /**
     * Main method to launch the AI-Agent Framework application.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiHub.class, args);
        log.info("application started");
    }

}