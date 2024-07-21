package org.example.domain.dto;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@Component
@DynamoDbBean
public class City {
    private String name;
    private List<Article> articles;

    public City() {
    }

    public City(String name, List<Article> articles) {
        this.name = name;
        this.articles = articles;
    }

    @DynamoDbPartitionKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> values) {
        this.articles = values;
    }

}
