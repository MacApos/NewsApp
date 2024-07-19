package org.example.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mapper.CityMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static java.nio.file.StandardOpenOption.*;


@Component
public class LoadDataTask implements Runnable {
    Map<String, String> cities = Map.of(
            "new-york", "new york",
            "ashburn", "ashburn",
            "hemingford", "hemingford ne"
    );

//    Map<String, String> cities = Map.of("new-york", "/1",
//            "ashburn", "/2",
//            "hemingford", "/3");

    private final UriComponentsBuilder newsApiUriBuilder;
    private final String[] newsApiUriHeaders;
    private final WebClient webClient;
    private final CityMapper cityMapper;

    public LoadDataTask(UriComponentsBuilder newsApiUriBuilder, String[] newsApiUriHeaders, WebClient webClient, CityMapper cityMapper) {
        this.newsApiUriBuilder = newsApiUriBuilder;
        this.newsApiUriHeaders = newsApiUriHeaders;
        this.webClient = webClient;
        this.cityMapper = cityMapper;
    }

    public Mono<String> getMonoString(String query) {
        Mono<String> mono = getResponseSpec(query).bodyToMono(String.class);
        return mono;
    }

    public Mono<Answer> getMonoAnswer(String query) {
        return getResponseSpec(query).bodyToMono(Answer.class);
    }

    private WebClient.ResponseSpec getResponseSpec(String query) {
        return webClient.get()
                .uri(newsApiUriBuilder.cloneBuilder()
                        .queryParam("q", query)
//                        .queryParam("postId", query)
                        .build()
                        .toUri())
                .header(newsApiUriHeaders[0], newsApiUriHeaders[1])
                .retrieve();
    }
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run() {

        Path path = Paths.get("src/main/resources/cities.txt");
        boolean fileExists = Files.exists(path);
        if (!fileExists) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Flux.fromIterable(cities.values())
                    .publishOn(Schedulers.boundedElastic())
                    .delayElements(Duration.ofMillis(500))
                    .flatMap(this::getMonoString)
                    .subscribe(s -> {
                        s = String.format("%s\n", s);
                        try {
                            Files.write(path, s.getBytes(), StandardOpenOption.APPEND);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } else {
            AsynchronousFileChannel fileChannel;
            ByteBuffer buffer;
            try {
                buffer = ByteBuffer.wrap(Files.readAllBytes(path));
                fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileChannel.read(buffer, 0, null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer integer, Void unused) {
                    String read = new String(buffer.array(), StandardCharsets.UTF_8);
                    Answer answer;
                    List<Answer> answers = new ArrayList<>();
                    for (String s : read.trim().split("\n")) {
                        try {
                            answer = objectMapper.readValue(read, Answer.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        answers.add(answer);
                    }
                }

                @Override
                public void failed(Throwable throwable, Void unused) {
                    System.err.println("fail");
                }
            });

        }

//        Flux.fromIterable(cities.values())
//                .publishOn(Schedulers.boundedElastic())
//                .delayElements(Duration.ofMillis(500))
//                .flatMap(this::getMonoAnswer)
//                .publishOn(Schedulers.boundedElastic())
//                .map(cityMapper::answerToCity)
//                .subscribe(s -> System.out.println(s.getName()));
    }
}
