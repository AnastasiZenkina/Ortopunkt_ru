package com.ortopunkt.medicalcrm.repository;
import com.ortopunkt.medicalcrm.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
