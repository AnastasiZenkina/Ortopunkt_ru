package com.example.medicalcrm.service;
import com.example.medicalcrm.dto.PostRequestDto;
import com.example.medicalcrm.entity.Post;
import com.example.medicalcrm.repository.PostRepository;
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

    public Post create(PostRequestDto dto) {
        Post post = dto.toEntity();
        return postRepository.save(post);
    }

}
