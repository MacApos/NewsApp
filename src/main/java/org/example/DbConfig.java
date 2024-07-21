package org.example;

import org.example.domain.dto.City;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
public class DbConfig {
    @Bean
    public TableSchema<City> tableSchema() {
        return TableSchema.fromBean(City.class);
    }

    @Bean
    public DynamoDbEnhancedAsyncClient enhancedClient() {
        Region region = Region.EU_CENTRAL_1;
        DynamoDbAsyncClient asyncClient = DynamoDbAsyncClient.builder().region(region).build();
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(asyncClient)
                .build();
    }

    @Bean
    public DynamoDbAsyncTable<City> table(DynamoDbEnhancedAsyncClient enhancedClient,
                                     TableSchema<City> tableSchema) {
        return enhancedClient.table("News", tableSchema);
    }
}
