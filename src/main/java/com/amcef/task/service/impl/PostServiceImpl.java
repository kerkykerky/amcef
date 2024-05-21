package com.amcef.task.service.impl;

import com.amcef.task.client.JsonPlaceholderClient;
import com.amcef.task.exception.PostNotFoundException;
import com.amcef.task.exception.UserNotFoundException;
import com.amcef.task.model.Post;
import com.amcef.task.repository.PostRepository;
import com.amcef.task.service.PostService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final JsonPlaceholderClient jsonPlaceholderClient;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, JsonPlaceholderClient jsonPlaceholderClient) {
        this.postRepository = postRepository;
        this.jsonPlaceholderClient = jsonPlaceholderClient;
    }

    @Transactional
    @Override
    public Post addPost(Post post) {
        try {
            jsonPlaceholderClient.getUserById(post.getUserId());
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundException("User with ID " + post.getUserId() + " not found");
        }
        return postRepository.save(post);
    }

    @Transactional
    @Override
    public Post getPostById(Integer id) {
        return postRepository.findById(id).orElseGet(() -> {
            Post externalPost = jsonPlaceholderClient.getPostsById(id);
            if (externalPost != null) {
                postRepository.save(externalPost);
                return externalPost;
            } else {
                throw new PostNotFoundException("Post with ID " + id + " not found");
            }
        });
    }

    @Override
    public List<Post> getPostsByUserId(Integer userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        if (posts.isEmpty()) {
            List<Post> externalPosts = jsonPlaceholderClient.getPostsByUserId(userId);
            if (!externalPosts.isEmpty()) {
                postRepository.saveAll(externalPosts);
                return externalPosts;
            } else {
                throw new PostNotFoundException("No posts found for userId " + userId);
            }
        }
        return posts;
    }

    @Transactional
    @Override
    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Post updatePost(Integer id, String title, String body) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found with ID " + id));
        post.setTitle(title);
        post.setBody(body);
        return postRepository.save(post);
    }
}
