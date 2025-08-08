package com.ortopunkt.medicalcrm.dto;
import com.ortopunkt.medicalcrm.entity.BotUser;
import lombok.Data;

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
