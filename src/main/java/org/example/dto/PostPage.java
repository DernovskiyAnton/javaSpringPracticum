package org.example.dto;

import org.example.model.Post;

import java.util.List;

public record PostPage(
        List<Post> posts,
        boolean hasPrev,
        boolean hasNext,
        int lastPage
) {
}