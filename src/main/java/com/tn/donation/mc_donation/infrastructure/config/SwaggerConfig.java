package com.tn.donation.mc_donation.infrastructure.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi donationApi() {
        return GroupedOpenApi.builder()
                .group("donation-api")
                .pathsToMatch("/campaigns/**", "/donors/**", "/donations/**")  // because of your context-path
                .build();
    }
}