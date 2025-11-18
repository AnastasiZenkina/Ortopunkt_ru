package com.ortopunkt.crm.service.post;

import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.crm.entity.Post;
import com.ortopunkt.crm.repository.PostRepository;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ServiceLogger log = new ServiceLogger(getClass(), "CRM");

    public List<Post> getAllPosts() {
        log.info("Запрошены все посты");
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        log.info("Получение поста по id=" + id);
        return postRepository.findById(id);
    }

    public Post savePost(Post post) {
        log.info("Сохранение поста с id=" + post.getId());
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        log.info("Удаление поста id=" + id);
        postRepository.deleteById(id);
    }

    @Transactional
    public PostResponseDto create(PostRequestDto dto) {
        log.info("Создание или обновление поста за месяц " + dto.getMonthYear());
        String month = dto.getMonthYear();
        Post post = postRepository.findAll().stream()
                .filter(p -> month != null && month.equalsIgnoreCase(p.getMonthYear()))
                .findFirst()
                .orElse(new Post());

        PostMapper.applyDtoToEntity(dto, post);
        post = postRepository.save(post);
        return PostMapper.toResponseDto(post);
    }

    public Optional<PostResponseDto> getLatestPost() {
        log.info("Получение последнего поста");
        return postRepository.findAll().stream()
                .reduce((first, second) -> second)
                .map(PostMapper::toResponseDto);
    }
}