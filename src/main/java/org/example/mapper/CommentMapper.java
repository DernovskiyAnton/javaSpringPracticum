package org.example.mapper;

import org.example.dto.CommentDto;
import org.example.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.id(),
                comment.text(),
                comment.postId()
        );
    }
}