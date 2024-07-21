package org.example.service;

import org.example.domain.Answer;
import org.example.domain.dto.City;
import org.example.mapper.CityMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class LoadDataService {
    private final UriComponentsBuilder newsApiUriBuilder;
    private final String[] newsApiUriHeaders;
    private final WebClient webClient;
    private final CityMapper cityMapper;
    private DynamoDbService dynamoDBService;

    public LoadDataService(UriComponentsBuilder newsApiUriBuilder, String[] newsApiUriHeaders, WebClient webClient, CityMapper cityMapper, DynamoDbService dynamoDBService) {
        this.newsApiUriBuilder = newsApiUriBuilder;
        this.newsApiUriHeaders = newsApiUriHeaders;
        this.webClient = webClient;
        this.cityMapper = cityMapper;
        this.dynamoDBService = dynamoDBService;
    }

    public Mono<Answer> getResponse(String query) {
        return webClient.get()
                .uri(newsApiUriBuilder.cloneBuilder()
                        .queryParam("q", query)
                        .build()
                        .toUri())
                .header(newsApiUriHeaders[0], newsApiUriHeaders[1])
                .retrieve()
                .bodyToMono(Answer.class);
    }
}
