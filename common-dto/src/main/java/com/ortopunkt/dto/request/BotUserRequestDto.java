package com.ortopunkt.dto.request;
import lombok.Data;

@Data

public class BotUserRequestDto {
    private Long telegramId;
    private String role;
    private String username;
}
