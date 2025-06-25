package com.ortopunkt.medicalcrm.service;
import com.ortopunkt.medicalcrm.dto.PostRequestDto;
import com.ortopunkt.medicalcrm.entity.Post;
import com.ortopunkt.medicalcrm.repository.PostRepository;
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
