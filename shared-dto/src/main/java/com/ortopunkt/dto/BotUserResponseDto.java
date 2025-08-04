package com.ortopunkt.dto;
import com.ortopunkt.medicalcrm.entity.BotUser;
import lombok.Data;

@Data

public class BotUserResponseDto {

    private Long telegramId;
    private String role;
    private String username;

    public static BotUserResponseDto fromEntity(BotUser botUser){
        BotUserResponseDto dto = new BotUserResponseDto();
        dto.setTelegramId(botUser.getTelegramId());
        dto.setRole(botUser.getRole());
        dto.setUsername(botUser.getUsername());
        return dto;
    }
}
