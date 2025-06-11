package com.example.medicalcrm.service;

import com.example.medicalcrm.dto.ApplicationRequestDto;
import com.example.medicalcrm.entity.Application;
import com.example.medicalcrm.repository.ApplicationRepository;
import com.example.medicalcrm.repository.CampaignRepository;
import com.example.medicalcrm.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PatientRepository patientRepository;
    private final CampaignRepository campaignRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    public Application create(ApplicationRequestDto dto) {
        Application application = dto.toEntity();

        if (dto.getPatientId() != null) {
            patientRepository.findById(dto.getPatientId()).ifPresent(application::setPatient);
        }

        if (dto.getCampaignId() != null) {
            campaignRepository.findById(dto.getCampaignId()).ifPresent(application::setCampaign);
        }

        return applicationRepository.save(application);
    }

}
