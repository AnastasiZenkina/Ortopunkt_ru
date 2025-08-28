package com.ortopunkt.crm.service;

import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.repository.ApplicationRepository;
import com.ortopunkt.crm.repository.CampaignRepository;
import com.ortopunkt.crm.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PatientRepository patientRepository;
    private final CampaignRepository campaignRepository;

    // теперь возвращаем DTO, а не сущности
    public List<ApplicationResponseDto> getAllApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public Long countApplications(LocalDate fromDate) {
        return applicationRepository.countByCreatedAtAfter(fromDate);
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
        Application application = new Application();
        application.setText(dto.getText());
        application.setSource(dto.getSource());
        application.setChannel(dto.getChannel());
        application.setTargetOrSpam(dto.isTargetOrSpam());
        application.setAdType(dto.getAdType());
        application.setCreative(dto.getCreative());

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

    public void updatePaymentStatus(Long appId, String paymentStatus) {
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
        app.setPaymentStatus(paymentStatus);
        applicationRepository.save(app);
    }

    private ApplicationResponseDto toResponseDto(Application application) {
        ApplicationResponseDto dto = new ApplicationResponseDto();
        dto.setId(application.getId());

        if (application.getPatient() != null) {
            PatientResponseDto pDto = new PatientResponseDto();
            pDto.setId(application.getPatient().getId());
            pDto.setName(application.getPatient().getName());
            pDto.setUsername(application.getPatient().getUsername());
            pDto.setTgId(application.getPatient().getTgId());
            dto.setPatient(pDto);
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
        dto.setAnsweredByHuman(application.isAnsweredByHuman());
        dto.setAnsweredByAi(application.isAnsweredByAi());

        return dto;
    }

}