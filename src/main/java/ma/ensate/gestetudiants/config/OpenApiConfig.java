package ma.ensate.gestetudiants.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestetudiants API")
                        .version("1.0")
                        .description("API documentation for Gestetudiants project"));
    }
}