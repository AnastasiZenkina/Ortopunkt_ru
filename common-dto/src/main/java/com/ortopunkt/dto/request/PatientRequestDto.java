package com.ortopunkt.dto.request;
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

}
