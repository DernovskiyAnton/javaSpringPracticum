package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.PostDto;
import org.example.dto.PostPage;
import org.example.mapper.PostMapper;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostPage getAllPosts(String search, int pageNumber, int pageSize) {
        List<PostDto> posts = postRepository.findAll(search, pageNumber, pageSize)
                .stream()
                .map(postMapper::toDto)
                .toList();

        int totalCount = postRepository.countAll(search);
        int lastPage = (int) Math.ceil((double) totalCount / pageSize);

        return new PostPage(posts, pageNumber > 1, pageNumber < lastPage, lastPage);
    }

    public PostDto getPostById(long id) {
        return postMapper.toDto(postRepository.findById(id));
    }
}