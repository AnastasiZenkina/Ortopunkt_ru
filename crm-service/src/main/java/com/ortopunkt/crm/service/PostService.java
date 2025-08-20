package com.ortopunkt.crm.service;

import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.crm.entity.Post;
import com.ortopunkt.crm.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // Теперь возвращает PostResponseDto
    public PostResponseDto create(PostRequestDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setPlatform(dto.getPlatform());
        post.setPostDate(dto.getPostDate());
        post.setReach(dto.getReach());
        post.setLikes(dto.getLikes());
        post.setComments(dto.getComments());
        post.setShares(dto.getShares());
        post.setVideoWatchRate(dto.getVideoWatchRate());
        post.setWeak(dto.isWeak());

        post = postRepository.save(post);
        return toResponseDto(post);
    }

    private static PostResponseDto toResponseDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setPlatform(post.getPlatform());
        dto.setPostDate(post.getPostDate());
        dto.setReach(post.getReach());
        dto.setLikes(post.getLikes());
        dto.setComments(post.getComments());
        dto.setShares(post.getShares());
        dto.setVideoWatchRate(post.getVideoWatchRate());
        dto.setWeak(post.isWeak());
        return dto;
    }
}
