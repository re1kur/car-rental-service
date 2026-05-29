package re1kur.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rentCarOpenApi() {
        return new OpenAPI().info(new Info()
                .title("RentCar REST API")
                .version("v1")
                .description("REST API for the car rental storefront: products, categories, cart and auth. "
                        + "Cart and order endpoints require an authenticated session (same session as the SSR site).")
                .license(new License().name("Educational project")));
    }
}
