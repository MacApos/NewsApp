import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class WebClientUnitTest {
    @TestConfiguration
    static class WebClientUnitTestConfiguration {

        @Bean
        public WebClient webClient() {
            return WebClient.create();
        }
    }

    @Autowired
    private WebClient webClient;

    @Value("${host}")
    private String uri;

    @Value("${apiKey}")
    private String apiKey;

    @Value("${cx}")
    private String cx;

    @Test
    void givenDifferentParameters_whenUsed_thenReturnResponse() {
        Flux<String> stringFlux = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(uri)
                                .queryParam("key", apiKey)
                                .queryParam("cx", cx)
                                .queryParam("q", "new york")
                                .build())
                .retrieve()
                .bodyToFlux(String.class);
        assertNotNull(stringFlux);

    }

}
