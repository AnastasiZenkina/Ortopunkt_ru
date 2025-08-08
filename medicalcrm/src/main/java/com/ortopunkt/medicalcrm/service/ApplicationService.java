package com.ortopunkt.medicalcrm.service;

import com.ortopunkt.medicalcrm.dto.ApplicationRequestDto;
import com.ortopunkt.dto.ApplicationResponseDto;
import com.ortopunkt.medicalcrm.entity.Application;
import com.ortopunkt.medicalcrm.repository.ApplicationRepository;
import com.ortopunkt.medicalcrm.repository.CampaignRepository;
import com.ortopunkt.medicalcrm.repository.PatientRepository;
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

    public ApplicationResponseDto create(ApplicationRequestDto dto) {
        Application application = dto.toEntity();

        if (dto.getPatientId() != null) {
            patientRepository.findById(dto.getPatientId()).ifPresent(application::setPatient);
        }

        if (dto.getCampaignId() != null) {
            campaignRepository.findById(dto.getCampaignId()).ifPresent(application::setCampaign);
        }

        application = applicationRepository.save(application);
        return toResponseDto(application);
    }

    public void markAsAnswered(Long appId) {
        Application app = applicationRepository.findById(appId).orElseThrow();
        app.setAnsweredByHuman(true);
        applicationRepository.save(app);
    }

    public Application getApplication(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
    }

    public boolean hasPhotos(Application app) {
        List<String> photos = app.getPhotoFileIds();
        return photos != null && !photos.isEmpty();
    }

    private ApplicationResponseDto toResponseDto(Application application) {
        ApplicationResponseDto dto = new ApplicationResponseDto();
        dto.setId(application.getId());

        if (application.getPatient() != null) {
            dto.setPatientId(application.getPatient().getId());
        }

        if (application.getCampaign() != null) {
            dto.setCampaignId(application.getCampaign().getId());
        }

        dto.setText(application.getText());
        dto.setStatus(application.getStatus());
        dto.setSource(application.getSource());
        dto.setChannel(application.getChannel());
        dto.setTargetOrSpam(application.isTargetOrSpam());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setAdType(application.getAdType());
        dto.setCreative(application.getCreative());

        return dto;
    }
}
