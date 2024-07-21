package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.domain.Answer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/query")
public class QueryController {

    private final RequestHeadersUriSpec<?> defaultGetRequest;

    public QueryController(RequestHeadersUriSpec<?> defaultGetRequest) {
        this.defaultGetRequest = defaultGetRequest;
    }

    public Mono<Answer> createFlux(String q) {
        ResponseSpec q1 = defaultGetRequest.uri(uriBuilder -> uriBuilder
//                        .queryParam("q", q)
                        .queryParam("q", q)
                        .build())
                .retrieve();
        Mono<Answer> answerMono = q1.bodyToMono(Answer.class);
        Answer block = answerMono.block();
        return answerMono;
    }

    HashMap<String, String> cities = new HashMap<>(Map.of("new-york", "new york",
            "ashburn", "ashburn",
            "hemingford", "hemingford ne"));

    @GetMapping("/{city}")
    public Mono<Answer> getPosts(@PathVariable("city") String requestCity) {
        Map.Entry<String, String> city = cities.entrySet().stream()
                .filter(entrySet -> entrySet.getKey().equals(requestCity))
                .findFirst().orElse(null);

        if (city != null) {
            Mono<Answer> flux = createFlux(city.getValue());
            return flux;
        } else {
            return null;
        }
    }

}