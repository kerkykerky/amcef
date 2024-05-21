package com.amcef.task.controller;

import com.amcef.task.model.Post;
import com.amcef.task.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void addPost_shouldReturnCreatedPost() throws Exception {
        Post post = new Post();
        post.setId(1);
        post.setUserId(1);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        when(postService.addPost(any(Post.class))).thenReturn(post);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1, \"title\": \"Test Title\", \"body\": \"Test Body\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.body").value("Test Body"));

        verify(postService, times(1)).addPost(any(Post.class));
    }

    @Test
    void getPostById_existingPost_shouldReturnPost() throws Exception {
        Post post = new Post();
        post.setId(1);
        post.setUserId(1);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        when(postService.getPostById(1)).thenReturn(post);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.body").value("Test Body"));

        verify(postService, times(1)).getPostById(1);
    }

    @Test
    void getPostsByUserId_existingPosts_shouldReturnPosts() throws Exception {
        Post post = new Post();
        post.setId(1);
        post.setUserId(1);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        when(postService.getPostsByUserId(1)).thenReturn(Collections.singletonList(post));

        mockMvc.perform(get("/posts/user")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$[0].body").value("Test Body"));

        verify(postService, times(1)).getPostsByUserId(1);
    }

    @Test
    void deletePost_shouldReturnNoContent() throws Exception {
        doNothing().when(postService).deletePost(1);

        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent());

        verify(postService, times(1)).deletePost(1);
    }

    @Test
    void updatePost_shouldReturnUpdatedPost() throws Exception {
        Post post = new Post();
        post.setId(1);
        post.setUserId(1);
        post.setTitle("Updated Title");
        post.setBody("Updated Body");

        when(postService.updatePost(eq(1), anyString(), anyString())).thenReturn(post);

        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Title\", \"body\": \"Updated Body\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.body").value("Updated Body"));

        verify(postService, times(1)).updatePost(eq(1), anyString(), anyString());
    }
}
