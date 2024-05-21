package com.amcef.task.controller;

import com.amcef.task.model.Post;
import com.amcef.task.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@Validated
@Tag(name = "Post Management", description = "Operations related to Post Management")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Add a new post", description = "Creates a new post and stores it in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully", content = @Content(schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Post> addPost(
            @Parameter(description = "Post to add", required = true)
            @Valid @RequestBody Post post) {
        Post createdPost = postService.addPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @Operation(summary = "Get post by ID", description = "Retrieves a post by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found", content = @Content(schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(
            @Parameter(description = "ID of the post to be retrieved", required = true)
            @PathVariable Integer id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @Operation(summary = "Get posts by user ID", description = "Retrieves all posts by a specific user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts found", content = @Content(schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404", description = "No posts found for user ID", content = @Content)
    })
    @GetMapping("/user")
    public ResponseEntity<List<Post>> getPostsByUserId(
            @Parameter(description = "ID of the user whose posts are to be retrieved", required = true)
            @RequestParam Integer userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @Operation(summary = "Delete a post", description = "Deletes a post by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "ID of the post to be deleted", required = true)
            @PathVariable Integer id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update a post", description = "Updates a post's title and body by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully", content = @Content(schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @Parameter(description = "ID of the post to be updated", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Updated post object", required = true)
            @RequestBody Post post) {
        Post updatedPost = postService.updatePost(id, post.getTitle(), post.getBody());
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }
}
