package com.ortopunkt.medicalcrm.dto;
import com.ortopunkt.medicalcrm.entity.Patient;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientResponseDto {
    private Long id;
    private String name;
    private String city;
    private String country;
    private String phone;
    private String email;
    private String vkId;
    private String tgId;
    private String instaId;
    private String typeOfPayment;
    private LocalDate operationDate;

    public static PatientResponseDto fromEntity(Patient patient) {
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
