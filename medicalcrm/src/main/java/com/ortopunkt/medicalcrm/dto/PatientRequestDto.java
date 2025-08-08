package com.ortopunkt.medicalcrm.dto;
import com.ortopunkt.medicalcrm.entity.Patient;
import lombok.Data;
import java.time.LocalDate;

@Data

public class PatientRequestDto {
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

    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setName(this.name);
        patient.setCity(this.city);
        patient.setCountry(this.country);
        patient.setPhone(this.phone);
        patient.setEmail(this.email);
        patient.setVkId(this.vkId);
        patient.setTgId(this.tgId);
        patient.setInstaId(this.instaId);
        patient.setTypeOfPayment(this.typeOfPayment);
        patient.setOperationDate(this.operationDate);

        return patient;
    }

}
