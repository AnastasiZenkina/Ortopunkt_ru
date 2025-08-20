package com.ortopunkt.crm.repository;
import com.ortopunkt.crm.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Long countByCreatedAtAfter(LocalDate fromDate);
}
