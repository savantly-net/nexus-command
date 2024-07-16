package net.savantly.security.config;

import java.util.List;

import lombok.Data;

@Data
public class CorsConfig {

    private List<String> allowedOrigins = List.of("*");
    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE");
    private List<String> allowedHeaders = List.of("*");
}
