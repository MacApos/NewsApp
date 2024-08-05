package org.example.service;

import org.example.domain.dto.City;
import org.example.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

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

    public Mono<City> putNewsIntoTable(String city, String state) {
//        Mono<City> cityMono = Mono.fromFuture(dynamoDbService.getNews(city));
        Mono<City> cityMono = Mono.empty();
        return cityMono.switchIfEmpty(loadDataService.validateCity(city, state))
                .doOnSuccess(success -> System.out.println("Write to database"));
        //        dynamoDbService::putNews
    }

}
