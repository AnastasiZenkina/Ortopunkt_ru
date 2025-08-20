package com.ortopunkt.crm.controller;
import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.crm.entity.Post;
import com.ortopunkt.crm.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.ortopunkt.dto.request.PostRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping
    public List <Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id){
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> create(@RequestBody @Valid PostRequestDto dto) {
        return ResponseEntity.ok(postService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
