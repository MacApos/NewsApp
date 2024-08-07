package com.newsapp24;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


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
    public WebClient webClient() {
        return WebClient.create();
    }

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
}
