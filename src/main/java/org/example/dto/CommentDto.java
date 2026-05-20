package org.example.dto;

public record CommentDto(
        long id,
        String text,
        long postId
) {
}
