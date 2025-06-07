package com.api.hub.llm.pojo;

import lombok.Data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(name = "llm.api.enable", havingValue = "true")
@ConfigurationProperties(prefix = "llm.api")
public class LLMApiProperties {
    private String baseUrl;      // e.g. https://api.openai.com/v1
    private String apiKey;
    private String organization; // optional, e.g. for OpenAI
}
