package com.ortopunkt.crm.service;

import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.repository.ApplicationRepository;
import com.ortopunkt.crm.repository.CampaignRepository;
import com.ortopunkt.crm.repository.PatientRepository;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PatientRepository patientRepository;
    private final CampaignRepository campaignRepository;
    private final ServiceLogger log = new ServiceLogger(getClass(), "CRM");

    @Value("${analytics.api.url}")
    private String analyticsApiUrl;

    public List<ApplicationResponseDto> getAllApplications() {
        log.info("Запрос всех заявок");
        return applicationRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public Long countApplications(LocalDate fromDate) {
        log.info("Подсчёт заявок с даты " + fromDate);
        return applicationRepository.countByCreatedAtAfter(fromDate);
    }

    public Optional<Application> getApplicationById(Long id) {
        log.info("Получение заявки id=" + id);
        return applicationRepository.findById(id);
    }

    public Application saveApplication(Application application) {
        log.info("Сохранение заявки id=" + application.getId());
        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        log.info("Удаление заявки id=" + id);
        applicationRepository.deleteById(id);
    }

    public ApplicationResponseDto create(ApplicationRequestDto dto) {
        log.info("Создание новой заявки");
        Application application = new Application();
        application.setText(dto.getText());
        application.setSource(dto.getSource());
        application.setChannel(dto.getChannel());
        application.setTargetOrSpam(dto.isTargetOrSpam());
        application.setAdType(dto.getAdType());
        application.setCreative(dto.getCreative());
        application.setCreatedAt(LocalDate.now());
        application.setPhotoFileIds(dto.getPhotoFileIds());

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
        log.info("Отметка заявки id=" + appId + " как отвеченной человеком");
        Application app = applicationRepository.findById(appId).orElseThrow();
        app.setAnsweredByHuman(true);
        applicationRepository.save(app);
    }

    public Application getApplication(Long id) {
        log.info("Поиск заявки id=" + id);
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
    }

    public boolean hasPhotos(Application app) {
        return app.getPhotoFileIds() != null && !app.getPhotoFileIds().isEmpty();
    }

    public void updateApplicationStatus(Long appId, String status) {
        log.info("Обновление статуса заявки id=" + appId);
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));

        String current = app.getStatus();
        if (current == null || current.isBlank()) current = "Записан";

        if ("Прооперирован платно".equalsIgnoreCase(status)) {
            app.setPaymentStatus("PAID");
        } else if ("Прооперирован по квоте".equalsIgnoreCase(status)) {
            app.setPaymentStatus("QUOTA");
        } else {
            app.setStatus(status);
        }

        applicationRepository.save(app);

        if ("PAID".equalsIgnoreCase(app.getPaymentStatus()) || "QUOTA".equalsIgnoreCase(app.getPaymentStatus())) {
            try {
                new Thread(() -> {
                    try {
                        var rest = new org.springframework.web.client.RestTemplate();
                        rest.postForObject(
                                analyticsApiUrl + "/api/analytics/applications/update",
                                toResponseDto(app),
                                String.class
                        );
                    } catch (Exception ignored) { }
                }).start();
            } catch (Exception e) {
                log.warn("Не удалось уведомить аналитику: " + e.getMessage());
            }
        }
    }

    public ApplicationResponseDto toResponseDto(Application application) {
        ApplicationResponseDto dto = new ApplicationResponseDto();
        dto.setId(application.getId());

        if (application.getPatient() != null) {
            var patient = patientRepository.findById(application.getPatient().getId()).orElse(null);
            if (patient != null) {
                PatientResponseDto pDto = new PatientResponseDto();
                pDto.setId(patient.getId());
                pDto.setName(patient.getName());
                pDto.setUsername(patient.getUsername());
                pDto.setTgId(patient.getTgId());
                dto.setPatient(pDto);
            }
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
        dto.setPhotoFileIds(application.getPhotoFileIds());
        dto.setPaymentStatus(application.getPaymentStatus());
        return dto;
    }
}