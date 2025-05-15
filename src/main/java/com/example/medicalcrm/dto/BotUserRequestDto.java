package com.example.medicalcrm.dto;
import com.example.medicalcrm.entity.BotUser;
import lombok.Data;
import java.time.LocalDate;

@Data

public class BotUserRequestDto {
    private String role;
    private String username;

    public BotUser toEntity(){
       BotUser botUser = new BotUser();
       botUser.setRole(this.role);
       botUser.setUsername(this.username);
       return botUser;
    }
}
