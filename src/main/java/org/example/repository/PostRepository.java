package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String COL_ID =             "id";
    private static final String COL_TITLE =          "title";
    private static final String COL_TEXT =           "text";
    private static final String COL_TAGS =           "tags";
    private static final String COL_LIKES_COUNT =    "likes_count";
    private static final String COL_COMMENTS_COUNT = "comments_count";

    private static final RowMapper<Post> POST_ROW_MAPPER = (rs, rowNum) -> new Post(
            rs.getLong(COL_ID),
            rs.getString(COL_TITLE),
            rs.getString(COL_TEXT),
            List.of(rs.getString(COL_TAGS).split(",")),
            rs.getInt(COL_LIKES_COUNT),
            rs.getInt(COL_COMMENTS_COUNT)
    );

    public List<Post> findAll(String search, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        return jdbcTemplate.query(
                "SELECT * FROM posts WHERE title LIKE ? OR text LIKE ? LIMIT ? OFFSET ?",
                POST_ROW_MAPPER,
                "%" + search + "%",
                "%" + search + "%",
                pageSize,
                offset
        );
    }

    public Post findById(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM posts WHERE id = ?",
                POST_ROW_MAPPER,
                id
        );
    }

    public int countAll(String search) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM posts WHERE title LIKE ? OR text LIKE ?",
                Integer.class,
                "%" + search + "%",
                "%" + search + "%"
        );
    }
}