package br.com.vcoroa.accountmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
            .title("Account Manager API")
            .version("1.0")
            .description("API para gerenciamento de contas a pagar e receber."));
    }

    @Bean
    public GroupedOpenApi storeOpenApi() {
        String packagesToScan[] = {"br.com.vcoroa.accountmanager.controllers"};
        return GroupedOpenApi.builder()
            .group("Account Manager API")
            .packagesToScan(packagesToScan)
            .build();
    }
}
