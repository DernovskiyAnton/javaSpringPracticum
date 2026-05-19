package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.PostDto;
import org.example.dto.PostPage;
import org.example.dto.PostRequest;
import org.example.exception.GlobalExceptionHandler;
import org.example.exception.ImageProcessingException;
import org.example.exception.ResourceNotFoundException;
import org.example.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
@ContextConfiguration(classes = {PostControllerTest.TestConfig.class})
class PostControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private PostController postController;

    @Autowired
    private ObjectMapper objectMapper;

    private PostDto testPostDto;
    private PostRequest testPostRequest;

    @Configuration
    static class TestConfig {
        @Bean
        public PostService postService() {
            return mock(PostService.class);
        }

        @Bean
        public PostController postController(PostService postService) {
            return new PostController(postService);
        }

        @Bean
        public GlobalExceptionHandler globalExceptionHandler() {
            return new GlobalExceptionHandler();
        }

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper;
        }
    }

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = objectMapper;
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .setMessageConverters(
                        new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(mapper),
                        new org.springframework.http.converter.ByteArrayHttpMessageConverter())
                .build();
        reset(postService);
        testPostDto = new PostDto(1L, "Test Title", "Test Text", List.of("tag1", "tag2"), 10, 5);
        testPostRequest = new PostRequest("Test Title", "Test Text", List.of("tag1", "tag2"));
    }

    @Test
    void findAll_shouldReturnPostPage() throws Exception {
        PostPage postPage = new PostPage(List.of(testPostDto), false, false, 1);
        when(postService.getAllPosts("test", 1, 10)).thenReturn(postPage);

        mockMvc.perform(get("/api/posts")
                        .param("search", "test")
                        .param("pageNumber", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].id").value(1))
                .andExpect(jsonPath("$.posts[0].title").value("Test Title"))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.lastPage").value(1));

        verify(postService).getAllPosts("test", 1, 10);
    }

    @Test
    void findAll_shouldUseDefaultParams() throws Exception {
        PostPage postPage = new PostPage(List.of(testPostDto), false, false, 1);
        when(postService.getAllPosts("", 1, 10)).thenReturn(postPage);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk());

        verify(postService).getAllPosts("", 1, 10);
    }

    @Test
    void findById_shouldReturnPost() throws Exception {
        when(postService.getPostById(1L)).thenReturn(testPostDto);

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.text").value("Test Text"))
                .andExpect(jsonPath("$.tags[0]").value("tag1"))
                .andExpect(jsonPath("$.likesCount").value(10))
                .andExpect(jsonPath("$.commentsCount").value(5));

        verify(postService).getPostById(1L);
    }

    @Test
    void findById_shouldReturn404_whenPostNotFound() throws Exception {
        when(postService.getPostById(999L)).thenThrow(new EmptyResultDataAccessException(1));

        mockMvc.perform(get("/api/posts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        verify(postService).getPostById(999L);
    }

    @Test
    void create_shouldReturnCreatedPost() throws Exception {
        when(postService.createPost(any(PostRequest.class))).thenReturn(testPostDto);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPostRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.text").value("Test Text"));

        verify(postService).createPost(any(PostRequest.class));
    }

    @Test
    void create_shouldReturn400_whenValidationFails() throws Exception {
        PostRequest invalidRequest = new PostRequest("", "", List.of());

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"));

        verify(postService, never()).createPost(any());
    }

    @Test
    void update_shouldReturnUpdatedPost() throws Exception {
        PostDto updatedDto = new PostDto(1L, "Updated Title", "Updated Text", List.of("tag3"), 10, 5);
        when(postService.update(eq(1L), any(PostRequest.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPostRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"));

        verify(postService).update(eq(1L), any(PostRequest.class));
    }

    @Test
    void update_shouldReturn404_whenPostNotFound() throws Exception {
        when(postService.update(eq(999L), any())).thenThrow(new ResourceNotFoundException("Post", 999L));

        mockMvc.perform(put("/api/posts/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPostRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(postService).update(eq(999L), any());
    }

    @Test
    void delete_shouldReturn200() throws Exception {
        doNothing().when(postService).delete(1L);

        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isOk());

        verify(postService).delete(1L);
    }

    @Test
    void delete_shouldReturn404_whenPostNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Post", 999L)).when(postService).delete(999L);

        mockMvc.perform(delete("/api/posts/999"))
                .andExpect(status().isNotFound());

        verify(postService).delete(999L);
    }

    @Test
    void addLike_shouldReturnUpdatedLikesCount() throws Exception {
        when(postService.addLike(1L)).thenReturn(11);

        mockMvc.perform(post("/api/posts/1/likes"))
                .andExpect(status().isOk())
                .andExpect(content().string("11"));

        verify(postService).addLike(1L);
    }

    @Test
    void addLike_shouldReturn404_whenPostNotFound() throws Exception {
        when(postService.addLike(999L)).thenThrow(new ResourceNotFoundException("Post", 999L));

        mockMvc.perform(post("/api/posts/999/likes"))
                .andExpect(status().isNotFound());

        verify(postService).addLike(999L);
    }

    @Test
    void updateImage_shouldReturn200() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        doNothing().when(postService).updateImage(eq(1L), any());

        mockMvc.perform(multipart("/api/posts/1/image")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(postService).updateImage(eq(1L), any());
    }

    @Test
    void updateImage_shouldReturn400_whenImageProcessingFails() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        doThrow(new ImageProcessingException("Failed to read image file"))
                .when(postService).updateImage(eq(1L), any());

        mockMvc.perform(multipart("/api/posts/1/image")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(postService).updateImage(eq(1L), any());
    }

    @Test
    void getImage_shouldReturnImageBytes() throws Exception {
        byte[] imageBytes = new byte[]{1, 2, 3, 4, 5};
        when(postService.getImage(1L)).thenReturn(imageBytes);

        mockMvc.perform(get("/api/posts/1/image"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageBytes));

        verify(postService).getImage(1L);
    }

    @Test
    void getImage_shouldReturn404_whenPostNotFound() throws Exception {
        when(postService.getImage(999L)).thenThrow(new EmptyResultDataAccessException(1));

        mockMvc.perform(get("/api/posts/999/image"))
                .andExpect(status().isNotFound());

        verify(postService).getImage(999L);
    }
}
