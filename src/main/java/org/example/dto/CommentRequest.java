package org.example.dto;

public record CommentRequest(
        String text,
        long postId
) {
}