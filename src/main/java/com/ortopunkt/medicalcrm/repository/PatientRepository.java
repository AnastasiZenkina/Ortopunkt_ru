package com.ortopunkt.medicalcrm.repository;
import com.ortopunkt.medicalcrm.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByTgId(String tgId);
}
