package org.example.controller;

import org.example.domain.Post;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final String uri = "https://jsonplaceholder.typicode.com/posts";
    private WebClient webClient;

    public PostController(WebClient webClient) {
        this.webClient = webClient;
    }

//    @GetMapping()
//    public Mono<Post> getPostById() {
//        return "";
//    }


    @GetMapping()
    public Flux<Post> getPosts() {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Post.class);
    }

}