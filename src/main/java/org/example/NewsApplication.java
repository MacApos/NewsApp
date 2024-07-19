package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class NewsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewsApplication.class, args);
    }

    @Value("${host}")
    String host;

    @Value("${pathSegments}")
    String pathSegments;

    @Value("${apiKey}")
    public String apiKey;

    @Bean
    public URI newsApiUri() {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(pathSegments)
                .queryParam("count", 50)
                .queryParam("mkt", "en-US")
                .queryParam("setLang", "en")
                .queryParam("sortBy", "date")
                .queryParam("originalImg", true)
                .build()
                .toUri();
        return uri;
    }

    @Bean
    public UriComponentsBuilder newsApiUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(pathSegments)
                .queryParam("count", 50)
                .queryParam("mkt", "en-US")
                .queryParam("setLang", "en")
                .queryParam("sortBy", "date")
                .queryParam("originalImg", true);
    }

    @Bean
    public String[] newsApiUriHeaders() {
        return new String[]{"Ocp-Apim-Subscription-Key", apiKey};
    }

    @Bean
    public WebClient.Builder webClientBuilder() throws MalformedURLException {
        return WebClient.builder()
                .baseUrl(newsApiUriBuilder().build().toUri().toURL().toString());
    }

    @Bean
    public WebClient webClient(){
        return WebClient.create();
    }

    @Bean
    public RequestHeadersUriSpec<?> createDefaultGetRequest(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build().get();
    }

    @Bean
    public RequestHeadersSpec<?> createRequestHeaders(RequestHeadersUriSpec<?> createDefaultPostRequest) {
        createDefaultPostRequest.header("Ocp-Apim-Subscription-Key", apiKey);
        return createDefaultPostRequest.header("Ocp-Apim-Subscription-Key", apiKey);
    }
}
