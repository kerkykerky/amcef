package com.amcef.task.integration;

import com.amcef.task.model.Post;
import com.amcef.task.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PostRepository postRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addPost_shouldReturnCreatedPost() throws Exception {
        Post post = new Post();
        post.setUserId(1);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        when(postRepository.save(any(Post.class))).thenReturn(post);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.body").value("Test Body"));

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void getPostById_existingPost_shouldReturnPost() throws Exception {
        Post post = new Post();
        post.setId(1);
        post.setUserId(1);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        when(postRepository.findById(1)).thenReturn(java.util.Optional.of(post));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.body").value("Test Body"));

        verify(postRepository, times(1)).findById(1);
    }

    @Test
    void getPostsByUserId_existingPosts_shouldReturnPosts() throws Exception {
        Post post = new Post();
        post.setId(1);
        post.setUserId(1);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        when(postRepository.findByUserId(1)).thenReturn(Collections.singletonList(post));

        mockMvc.perform(get("/posts/user")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$[0].body").value("Test Body"));

        verify(postRepository, times(1)).findByUserId(1);
    }
}