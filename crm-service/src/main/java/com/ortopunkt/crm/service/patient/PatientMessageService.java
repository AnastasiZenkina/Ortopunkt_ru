package com.ortopunkt.crm.service.patient;

import com.ortopunkt.crm.entity.Application;
import com.ortopunkt.crm.entity.Patient;
import com.ortopunkt.crm.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientMessageService {

    private final PatientRepository patientRepository;

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
            patient = patientRepository.save(patient);
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

        patient = patientRepository.save(patient);

        Application app = new Application();
        app.setPatient(patient);
        app.setCreatedAt(LocalDate.now());
        app.setText(text != null ? text : "");

        PhotoSize largest = message.getPhoto().stream()
                .max((p1, p2) -> Integer.compare(p1.getFileSize(), p2.getFileSize()))
                .orElse(null);

        List<String> fileIds = new ArrayList<>();
        if (largest != null) {
            fileIds.add(largest.getFileId());
        }
        app.setPhotoFileIds(fileIds);

        app.setPatient(patientRepository.save(patient));
        app.getPatient().setId(patient.getId());

        return app;
    }

    public Application processGroupedPhotos(Message message, String text, List<String> fileIds) {
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

        patient = patientRepository.save(patient);

        Application app = new Application();
        app.setPatient(patient);
        app.setCreatedAt(LocalDate.now());
        app.setText(text != null ? text : "");
        app.setPhotoFileIds(fileIds);

        app.setPatient(patientRepository.save(patient));
        app.getPatient().setId(patient.getId());

        return app;
    }

    public void createOrAttachApplication(Long chatId, String fullName, String username, String text) {
        processMessage(chatId, username, fullName, text);
    }
}