package org.example.config;

import org.example.mapper.CommentMapper;
import org.example.mapper.PostMapper;
import org.example.repository.CommentRepository;
import org.example.repository.PostRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan
public class UnitTestConfig {
    @Bean
    @Primary
    public PostRepository mockPostRepository() {
        return Mockito.mock(PostRepository.class);
    }

    @Bean
    @Primary
    public PostMapper mockPostMapper() {
        return Mockito.mock(PostMapper.class);
    }

    @Bean
    @Primary
    public CommentRepository mockCommentRepository() {
        return Mockito.mock(CommentRepository.class);
    }

    @Bean
    @Primary
    public CommentMapper mockCommentMapper() {
        return Mockito.mock(CommentMapper.class);
    }
}
