package com.example.medicalcrm.repository;
import com.example.medicalcrm.entity.Patient;
import com.example.medicalcrm.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
