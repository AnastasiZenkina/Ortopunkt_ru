package com.ortopunkt.crm.service;

import com.ortopunkt.dto.request.ApplicationRequestDto;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.entity.Patient;
import com.ortopunkt.crm.repository.ApplicationRepository;
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
    private final PatientService patientService;
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

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public ApplicationResponseDto create(ApplicationRequestDto dto) {
        log.info("Создание новой заявки");

        Patient patient = patientRepository.findByTgId(dto.getTgId())
                .orElse(new Patient());

        patient.setTgId(dto.getTgId());
        patient.setName(dto.getName());
        patient.setUsername(dto.getUsername());

        patient = patientService.savePatient(patient);

        Application application = new Application();
        application.setPatient(patient);
        application.setText(dto.getText());
        application.setCreatedAt(LocalDate.now());
        application.setPhotoFileIds(dto.getPhotoFileIds());

        application = applicationRepository.save(application);
        return toResponseDto(application);
    }

    public void updateApplicationStatus(Long appId, String status) {
        log.info("Обновление статуса заявки id=" + appId);
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
        app.setStatus(status);

        if ("AI_ANSWERED".equals(status)) {
            app.setAnsweredByAi(true);
        }

        applicationRepository.save(app);
    }

    public ApplicationResponseDto toResponseDto(Application application) {
        ApplicationResponseDto dto = new ApplicationResponseDto();
        dto.setId(application.getId());

        Patient patient = application.getPatient();
        if (patient != null) {
            dto.setPatient(patientService.toResponseDto(patient));
        }

        dto.setText(application.getText());
        dto.setStatus(application.getStatus());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setAnsweredByHuman(application.isAnsweredByHuman());
        dto.setAnsweredByAi(application.isAnsweredByAi());
        dto.setPhotoFileIds(application.getPhotoFileIds());

        return dto;
    }
}