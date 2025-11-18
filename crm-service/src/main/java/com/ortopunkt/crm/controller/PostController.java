package com.ortopunkt.crm.controller;

import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.crm.entity.Post;
import com.ortopunkt.crm.service.post.PostService;
import com.ortopunkt.crm.service.post.PostManualService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.ortopunkt.dto.request.PostRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final PostManualService postManualService;

    @Autowired
    public PostController(PostService postService, PostManualService postManualService) {
        this.postService = postService;
        this.postManualService = postManualService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/latest")
    public ResponseEntity<PostResponseDto> getLatestPost() {
        Optional<PostResponseDto> dto = postService.getLatestPost();
        return dto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> create(@RequestBody @Valid PostRequestDto dto) {
        return ResponseEntity.ok(postService.create(dto));
    }

    @PostMapping("/manual/{platform}")
    public ResponseEntity<Void> saveManual(@PathVariable("platform") String platform,
                                           @RequestBody PostRequestDto dto) {
        postManualService.saveManual(dto, platform);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}