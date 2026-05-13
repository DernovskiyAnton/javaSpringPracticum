package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PostDto;
import org.example.dto.PostPage;
import org.example.model.Post;
import org.example.service.PostService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public PostPage findAll(
            @RequestParam("search") String search,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize) {
        return postService.getAllPosts(search, pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public PostDto findById(@PathVariable("id") long id) {
        return postService.getPostById(id);
    }
}