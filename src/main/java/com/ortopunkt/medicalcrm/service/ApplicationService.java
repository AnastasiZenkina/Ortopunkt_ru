package com.ortopunkt.medicalcrm.service;

import com.ortopunkt.medicalcrm.dto.ApplicationRequestDto;
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

}
