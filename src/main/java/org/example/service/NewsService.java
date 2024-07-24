package org.example.service;

import org.example.domain.dto.City;
import org.example.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
        Mono<City> cityMono = Mono.fromFuture(dynamoDbService.getNews(city));
        Mono<City> cityMono1 = cityMono
                .switchIfEmpty(loadDataService.getResponse(city).map(cityMapper::answerToCity))
                .doOnNext(c -> cityMono.hasElement().subscribe(hasElements -> {
                    if (!hasElements) {
                        dynamoDbService.putNews(c);
                    }
                }));

        return cityMono1;
    }

}
