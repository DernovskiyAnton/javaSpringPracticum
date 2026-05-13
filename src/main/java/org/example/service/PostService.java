package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.PostPage;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostPage getAllPosts(String search, int pageNumber, int pageSize) {
        List<Post> posts = postRepository.findAll(search, pageNumber, pageSize);
        int totalCount = postRepository.countAll(search);
        int lastPage = (int) Math.ceil((double) totalCount / pageSize);

        return new PostPage(
                posts,
                pageNumber > 1,
                pageNumber < lastPage,
                lastPage
        );
    }

    public Post getPostById(long id) {
        return postRepository.findById(id);
    }
}