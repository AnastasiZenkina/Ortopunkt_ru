package com.example.medicalcrm.repository;
import com.example.medicalcrm.entity.BotUser;
import com.example.medicalcrm.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotUserRepository extends JpaRepository<BotUser, Long> {
}
