package com.amcef.task.service;

import com.amcef.task.model.Post;

import java.util.List;

public interface PostService {

    Post addPost(Post post);

    Post getPostById(Integer id);

    List<Post> getPostsByUserId(Integer userId);

    void deletePost(Integer id);

    Post updatePost(Integer id, String title, String body);
}
