package com.ortopunkt.crm.repository;
import com.ortopunkt.crm.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
