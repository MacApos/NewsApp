package org.example.controller;

import org.example.domain.dto.City;
import org.example.service.NewsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @RequestMapping("/{city}")
    public Mono<City> newsController(@PathVariable String city)  {
        return newsService.putNewsIntoTable(city);
    }
}