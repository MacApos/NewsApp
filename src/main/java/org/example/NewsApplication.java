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

@SpringBootApplication
public class NewsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewsApplication.class, args);
    }

    @Value("${newsHost}")
    String newsHost;

    @Value("${newsPath}")
    String newsPath;

    @Value("${newsApiKey}")
    public String newsApiKey;

    @Value("${cityHost}")
    String cityHost;

    @Value("${cityPath}")
    String cityPath;

    @Value("${cityApiKey}")
    public String cityApiKey;


    @Bean
    public UriComponentsBuilder newsApiUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(newsHost)
                .path(newsPath)
                .queryParam("count", 20)
                .queryParam("mkt", "en-US")
                .queryParam("setLang", "en")
                .queryParam("sortBy", "date")
                .queryParam("originalImg", true);
    }

    @Bean
    public UriComponentsBuilder geocodingApiUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(cityHost)
                .path(cityPath)
                .queryParam("appid", cityApiKey);
    }

    @Bean
    public String[] newsApiUriHeaders() {
        return new String[]{"Ocp-Apim-Subscription-Key", newsApiKey};
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
        createDefaultPostRequest.header("Ocp-Apim-Subscription-Key", newsApiKey);
        return createDefaultPostRequest.header("Ocp-Apim-Subscription-Key", newsApiKey);
    }
}
