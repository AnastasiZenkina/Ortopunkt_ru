package com.ortopunkt.crm.service;

import com.ortopunkt.dto.request.PatientRequestDto;
import com.ortopunkt.dto.response.PatientResponseDto;
import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.entity.Patient;
import com.ortopunkt.crm.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public PatientResponseDto create(PatientRequestDto dto) {
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

    public Patient getByTelegramId(String tgId) {
        return patientRepository.findByTgId(tgId).orElse(null);
    }

    public Application processMessage(Long chatId, String username, String fullName, String text) {
        Patient patient = getByTelegramId(chatId.toString());
        if (patient == null) {
            patient = new Patient();
            patient.setTgId(chatId.toString());
            patient.setName(fullName.isBlank() ? "Без имени" : fullName);
            patient.setUsername(username);
            patient = savePatient(patient);
        }

        Application app = new Application();
        app.setText(text);
        app.setPatient(patient);
        app.setCreatedAt(LocalDate.now());

        return app;
    }

    public Application processPhotoMessage(Message message, String text) {
        Long chatId = message.getChatId();
        String username = message.getFrom().getUserName();
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();

        String fullName = (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "");

        Patient patient = patientRepository.findByTgId(chatId.toString())
                .orElseGet(() -> {
                    Patient newPatient = new Patient();
                    newPatient.setTgId(chatId.toString());
                    newPatient.setUsername(username);
                    newPatient.setName(fullName);
                    return patientRepository.save(newPatient);
                });

        Application app = new Application();
        app.setPatient(patient);
        app.setCreatedAt(LocalDate.now());
        app.setText(text != null ? text: "");

        PhotoSize largest = message.getPhoto().stream()
                .max((p1, p2) -> Integer.compare(p1.getFileSize(), p2.getFileSize()))
                .orElse(null);

        List<String> fileIds = new ArrayList<>();
        if (largest != null) {
            fileIds.add(largest.getFileId());
        }
        app.setPhotoFileIds(fileIds);

        return app;
    }

    public Application processGroupedPhotos(Message message, String text, List<String> fileIds) {
        Long chatId = message.getChatId();
        String username = message.
                getFrom().getUserName();
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();

        String fullName = (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "");

        Patient patient = patientRepository.findByTgId(chatId.toString())
                .orElseGet(() -> {
                    Patient newPatient = new Patient();
                    newPatient.setTgId(chatId.toString());
                    newPatient.setUsername(username);
                    newPatient.setName(fullName);
                    return patientRepository.save(newPatient);
                });

        Application app = new Application();
        app.setPatient(patient);
        app.setCreatedAt(LocalDate.now());
        app.setText(text != null ? text : "");
        app.setPhotoFileIds(fileIds);

        return app;
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
