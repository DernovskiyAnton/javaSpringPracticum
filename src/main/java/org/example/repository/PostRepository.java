package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Post> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM posts",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        List.of(rs.getString("tags").split(",")),
                        rs.getInt("likes_count"),
                        rs.getInt("comments_count")
                )
        );
    }

}
