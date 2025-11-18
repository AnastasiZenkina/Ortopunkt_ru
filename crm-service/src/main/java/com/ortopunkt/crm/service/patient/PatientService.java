package com.ortopunkt.crm.service.patient;

import com.ortopunkt.dto.request.PatientRequestDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.crm.entity.Patient;
import com.ortopunkt.crm.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient savePatient(Patient patient) {
        if (patient.getUsername() == null || patient.getUsername().isBlank()) {
            patient.setUsername("unknown");
        }
        if (patient.getName() == null || patient.getName().isBlank()) {
            patient.setName("Без имени");
        }
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public PatientResponseDto createDto(PatientRequestDto dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setCity(dto.getCity());
        patient.setCountry(dto.getCountry());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        patient.setVkId(dto.getVkId());
        patient.setTgId(dto.getTgId());
        patient.setInstaId(dto.getInstaId());
        patient.setTypeOfPayment(dto.getTypeOfPayment());
        patient.setOperationDate(dto.getOperationDate());

        patient = patientRepository.save(patient);
        return toResponseDto(patient);
    }

    private PatientResponseDto toResponseDto(Patient patient) {
        PatientResponseDto dto = new PatientResponseDto();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setCity(patient.getCity());
        dto.setCountry(patient.getCountry());
        dto.setPhone(patient.getPhone());
        dto.setEmail(patient.getEmail());
        dto.setVkId(patient.getVkId());
        dto.setTgId(patient.getTgId());
        dto.setInstaId(patient.getInstaId());
        dto.setTypeOfPayment(patient.getTypeOfPayment());
        dto.setOperationDate(patient.getOperationDate());
        return dto;
    }
}