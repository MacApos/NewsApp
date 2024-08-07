package com.newsapp24.service;

import com.newsapp24.domain.dto.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class NewsService {
    private final LoadDataService loadDataService;
    private final DynamoDbService dynamoDbService;

    @Autowired
    public NewsService(LoadDataService loadDataService, DynamoDbService dynamoDbService) {
        this.loadDataService = loadDataService;
        this.dynamoDbService = dynamoDbService;
    }

    public Mono<City> putNewsIntoTable(String city, String state) {
        Mono<City> cityMono = Mono.fromFuture(dynamoDbService.getNews(city));
        Mono<City> cityMono1 = Mono.empty();
        return loadDataService.validateCity(city, state)
                .doOnSuccess(success -> System.out.println("Write to database"));
        //        dynamoDbService::putNews
    }

}
