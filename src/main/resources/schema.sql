CREATE TABLE IF NOT EXISTS posts (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    text           TEXT         NOT NULL,
    tags           VARCHAR(255),
    likes_count    INT DEFAULT 0,
    comments_count INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS comments (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    text    TEXT   NOT NULL,
    post_id BIGINT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

INSERT INTO posts (title, text, tags, likes_count, comments_count)
VALUES ('Первый пост', 'Текст первого поста', 'tag_1,tag_2', 5, 1);

INSERT INTO posts (title, text, tags, likes_count, comments_count)
VALUES ('Второй пост', 'Текст второго поста', 'tag_3', 3, 0);