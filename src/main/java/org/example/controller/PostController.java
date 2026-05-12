package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Post;
import org.example.service.PostService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<Post> findAll() {
        return postService.getAllPosts();
    }
}