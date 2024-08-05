package org.example.service;

import com.amazonaws.services.dynamodbv2.xspec.S;
import org.example.domain.Answer;
import org.example.domain.dto.City;
import org.example.domain.dto.ValidatedCity;
import org.example.mapper.CityMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoadDataService {
    private final UriComponentsBuilder newsApiUriBuilder;
    private final String[] newsApiUriHeaders;
    private final UriComponentsBuilder geocodingApiUriBuilder;
    private final WebClient webClient;
    private final CityMapper cityMapper;
    private DynamoDbService dynamoDBService;

    public LoadDataService(UriComponentsBuilder newsApiUriBuilder, String[] newsApiUriHeaders, UriComponentsBuilder geocodingApiUriBuilder, WebClient webClient, CityMapper cityMapper, DynamoDbService dynamoDBService) {
        this.newsApiUriBuilder = newsApiUriBuilder;
        this.newsApiUriHeaders = newsApiUriHeaders;
        this.geocodingApiUriBuilder = geocodingApiUriBuilder;
        this.webClient = webClient;
        this.cityMapper = cityMapper;
        this.dynamoDBService = dynamoDBService;
    }

//    private URI buildUri(UriComponentsBuilder builder, Map<String, String> params, String path) {
//        String query = params.entrySet().stream()
//                .map(param -> String.format("%s=%s", param.getKey(), param.getValue()))
//                .collect(Collectors.joining(","));
//        return builder.cloneBuilder()
//                .query(query)
//                .path(path == null ? "" : path)
//                .build()
//                .toUri();
//    }

    private URI buildUri(UriComponentsBuilder builder, String path, Map<String, String> params) {
        UriComponentsBuilder cloneBuilder = builder.cloneBuilder();
        params.forEach(cloneBuilder::queryParam);
        return cloneBuilder
                .path(path == null ? "" : path)
                .build().toUri();
    }

    public Mono<Answer> getResponse(String query) {
        return webClient.get()
                .uri(buildUri(newsApiUriBuilder, null, Map.of("q", query)))
                .header(newsApiUriHeaders[0], newsApiUriHeaders[1])
                .retrieve()
                .bodyToMono(Answer.class);
    }

    public Mono<City> validateCity(String city, String state) {
        String cityQuery = String.join(",", List.of(city, state, "US"));
        return webClient.get()
                .uri(buildUri(geocodingApiUriBuilder, "/direct", Map.of("q", cityQuery)))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, String>>>() {
                }).flatMap(results -> {
                    if (results.isEmpty()) {
                        System.out.println("City not found");
                        return Mono.error(new Exception("City not found"));
                    } else {
                        Map<String, String> cityDataMap = results.get(0);
//                                getResponse(String.format("%s,%s",
//                                                cityDataMap.get("name"), cityDataMap.get("state")))
//                                        .map(cityMapper::answerToCity);
                        return Mono.just(new City(cityDataMap.get("name"), cityDataMap.get("state")));
                    }
                });
    }

}
