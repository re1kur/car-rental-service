package re1kur.rentalservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${custom.rest-clients.fileStore}")
    private String fileStoreUrl;

    @Bean
    WebClient fileStoreClient() {
        return WebClient.builder()
                .baseUrl(fileStoreUrl)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(1024 * 1024)
                )
                .build();
    }
}
