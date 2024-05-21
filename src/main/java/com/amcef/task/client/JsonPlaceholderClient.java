package com.amcef.task.client;

import com.amcef.task.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "jsonPlaceholderClient", url = "https://jsonplaceholder.typicode.com")
public interface JsonPlaceholderClient {

    @GetMapping("/users/{id}")
    void getUserById(@PathVariable("id") Integer id);

    @GetMapping("/posts/{id}")
    Post getPostsById(@PathVariable("id") Integer id);

    @GetMapping("/posts")
    List<Post> getPostsByUserId(@RequestParam("userId") Integer userId);
}
