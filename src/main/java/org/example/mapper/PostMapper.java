package org.example.mapper;

import org.example.dto.PostDto;
import org.example.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDto toDto(Post post) {
        return new PostDto(
                post.id(),
                post.title(),
                post.text(),
                post.tags(),
                post.likesCount(),
                post.commentsCount()
        );
    }
}