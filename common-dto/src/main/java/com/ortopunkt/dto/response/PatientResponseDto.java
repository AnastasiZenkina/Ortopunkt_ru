package com.ortopunkt.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientResponseDto {
    private Long id;
    private String name;
    private String username;
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
