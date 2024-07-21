package org.example.service;

import org.example.domain.dto.City;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.concurrent.CompletableFuture;


@Service
public class DynamoDbService {
    private final DynamoDbAsyncTable<City> table;

    public DynamoDbService(DynamoDbAsyncTable<City> table) {
        this.table = table;
    }

    public CompletableFuture<City> getNews(String cityName){
        Key key = Key.builder().partitionValue(cityName).build();
        return table.getItem(key);
    }

    public void putNews(City news){
        table.putItem(news);
    }

    public void deleteNews(String cityName){

    }

    public void updateNews(){

    }



}
