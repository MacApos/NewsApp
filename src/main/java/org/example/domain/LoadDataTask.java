package org.example.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mapper.CityMapper;
import org.example.service.DynamoDbService;
import org.example.service.LoadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LoadDataTask implements Runnable {
    public static Map<String, String> cities = Map.of(
            "new-york", "new york",
//            "ashburn", "ashburn",
            "hemingford", "hemingford ne"
    );

    Path path = Paths.get("src/main/resources/cities.txt");
    ObjectMapper objectMapper = new ObjectMapper();

    private final LoadDataService loadDataService;
    private final DynamoDbService dynamoDbService;
    private final CityMapper cityMapper;

    @Autowired
    public LoadDataTask(LoadDataService loadDataService, CityMapper cityMapper, DynamoDbService dynamoDbService) {
        this.loadDataService = loadDataService;
        this.cityMapper = cityMapper;
        this.dynamoDbService = dynamoDbService;
        savaData();
    }

    private void savaData() {
        boolean fileExists = Files.exists(path);
        if (!fileExists) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<Answer> block = Flux.fromIterable(cities.values())
                    .publishOn(Schedulers.boundedElastic())
                    .delayElements(Duration.ofMillis(500))
                    .flatMap(loadDataService::getResponse)
                    .collectList().blockOptional().orElseThrow(RuntimeException::new);

            block.forEach(answer -> {
                try {
                    String str = String.format("%s\n", objectMapper.writeValueAsString(answer));
                    Files.write(path, str.getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private List<Answer> mockData() {
        List<Answer> answers = new ArrayList<>();
        try {
            for (String lane : Files.readAllLines(path)) {
                answers.add(objectMapper.readValue(lane, Answer.class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return answers;
    }


    @Override
    public void run() {
        Flux.fromIterable(mockData())
                .delayElements(Duration.ofMillis(1000))
                .map(cityMapper::answerToCity)
                .subscribe(dynamoDbService::putNews);

//        Flux.fromIterable(cities.values())
//                .publishOn(Schedulers.boundedElastic())
//                .delayElements(Duration.ofMillis(500))
//                .flatMap(loadDataService::getResponse)
//                .publishOn(Schedulers.boundedElastic())
//                .map(cityMapper::answerToCity)
//                .subscribe(dynamoDbService::putNews);
    }
}
