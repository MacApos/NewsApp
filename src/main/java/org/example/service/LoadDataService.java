package org.example.service;

import org.example.domain.Answer;
import org.example.domain.dto.City;
import org.example.mapper.CityMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public class LoadDataService {
    private final WebClient webClient;
    private final UriComponentsBuilder newsApiUriBuilder;
    private final UriComponentsBuilder geocodingApiUriBuilder;
    private final String[] newsApiUriHeaders;
    private final CityMapper cityMapper;
    private final Validator validator;
    private final ResponseStatusException cityNotFound =
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "City not found");
    private final String countryCode = "US";


    public LoadDataService(WebClient webClient, UriComponentsBuilder newsApiUriBuilder,
                           UriComponentsBuilder geocodingApiUriBuilder, String[] newsApiUriHeaders, CityMapper cityMapper, Validator validator) {
        this.webClient = webClient;
        this.newsApiUriBuilder = newsApiUriBuilder;
        this.geocodingApiUriBuilder = geocodingApiUriBuilder;
        this.newsApiUriHeaders = newsApiUriHeaders;
        this.cityMapper = cityMapper;
        this.validator = validator;
    }

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
        return Mono.just(new City(city, state))
                .flatMap(initialCity -> {
                    Errors errors = new BeanPropertyBindingResult(
                            initialCity,
                            City.class.getName()
                    );
                    validator.validate(initialCity, errors);
                    if (errors.getAllErrors().isEmpty()) {
                        String query = initialCity.prepareQuery(countryCode);
                        return webClient.get()
                                .uri(buildUri(geocodingApiUriBuilder, "/direct", Map.of("q", query)))
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<List<City>>() {
                                });
                    } else {
                        return Mono.error(cityNotFound);
                    }
                })
                .flatMap(results -> {
                    if (results.isEmpty()) {
                        return Mono.error(cityNotFound);
                    } else {
//                                getResponse(results.get(0).prepareQuery()
//                                        .map(cityMapper::answerToCity);
                        return Mono.just(results.get(0));
                    }
                });
    }

}
