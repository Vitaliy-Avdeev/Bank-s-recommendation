package org.skypro.recommendService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    private final Environment environment;

    public OpenAPIConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAPI defineOpenAPI() {
        Server server = new Server();
        String serverUrl = environment.getProperty("api.server.url");
        server.setUrl(serverUrl);
        server.setDescription("Development");

        Info info = new Info()
                .title("Сервис для работы с банковскими продуктами.")
                .version("0.0.1-SNAPSHOT")
                .description("Сервис предназначен для сотрудников банка. Он позволяет выполнить персонализированный подбор услуг и рекомендовать новые банковские продукты/услуги клиентам банка.");
        return new OpenAPI().info(info).servers(List.of(server));
    }
}

