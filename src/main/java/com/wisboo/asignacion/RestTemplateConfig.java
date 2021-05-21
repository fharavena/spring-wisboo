package com.wisboo.asignacion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean("clienteRest")
    public RestTemplate registrarTemplate() {
        return new RestTemplate();
    }
}
