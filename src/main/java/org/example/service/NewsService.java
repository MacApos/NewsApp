package org.example.service;

import org.example.domain.Answer;
import org.example.domain.dto.City;
import org.example.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class NewsService {
    private final LoadDataService loadDataService;
    private final DynamoDbService dynamoDbService;
    private final CityMapper cityMapper;

    @Autowired
    public NewsService(LoadDataService loadDataService, CityMapper cityMapper, DynamoDbService dynamoDbService) {
        this.loadDataService = loadDataService;
        this.cityMapper = cityMapper;
        this.dynamoDbService = dynamoDbService;
    }

    public Mono<City> putNewsIntoTable(String city) {
        System.out.println(Thread.currentThread().getName());
        CompletableFuture<City> objectCompletableFuture = dynamoDbService.getNews(city).thenApply(s -> {
            if (s == null) {
                Mono<Answer> response = loadDataService.getResponse(city);
                Mono<City> cityMono = response.map(answer->{
                    System.out.println(Thread.currentThread().getName());
                    return cityMapper.answerToCity(answer);
                });
                cityMono.subscribe(c->{
                    System.out.println(Thread.currentThread().getName());
                    dynamoDbService.putNews(c);
                });
                return cityMono.block();
            }
            return s;
        });

        return Mono.fromFuture(objectCompletableFuture);
    }

}
