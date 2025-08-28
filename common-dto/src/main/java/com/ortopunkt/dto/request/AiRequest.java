package com.ortopunkt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiRequest {
    private String text;
    private boolean hasPhoto;
    private boolean answeredByHuman;
}
