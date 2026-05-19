package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.config.TestConfig;
import org.example.dto.PostRequest;
import org.example.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
@SpringJUnitConfig(TestConfig.class)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    Post post = new Post(
            0L,

            "Spring Boot",
            "Post text",
            List.of("java", "spring"),
            0,
            0
    );

    @Test
    void shouldSavePost() {

        Post saved = postRepository.save(post);

        assertNotNull(saved);
        assertNotNull(saved.id());

        assertEquals("Spring Boot", saved.title());
        assertEquals("Post text", saved.text());
        assertEquals(List.of("java", "spring"), saved.tags());

        assertEquals(0, saved.likesCount());
        assertEquals(0, saved.commentsCount());
    }

    @Test
    void shouldUpdatePost() {

        PostRequest updatedPost = new PostRequest(
                "New title",
                "New text",
                List.of("newTag1", "newTag2")
        );

        Post saved = postRepository.save(post);
        postRepository.update(saved.id(), updatedPost);

        Post actual = postRepository.findById(saved.id());

        assertNotNull(actual);
        assertEquals(saved.id(), actual.id());


        assertEquals("New title", actual.title());
        assertEquals("New text", actual.text());
        assertEquals(
                List.of("newTag1", "newTag2"),
                actual.tags());

        assertEquals(0, actual.likesCount());
        assertEquals(0, actual.commentsCount());
    }

    @Test
    void shouldDeletePost() {

        Post saved = postRepository.save(post);
        postRepository.deleteById(saved.id());

        assertThrows(
                EmptyResultDataAccessException.class,
                () -> postRepository.findById(saved.id())
        );
    }


}