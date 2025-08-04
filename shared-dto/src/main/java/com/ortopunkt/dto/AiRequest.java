package com.ortopunkt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiRequest {
    private String text;
    private boolean hasPhotos;
    private boolean answeredByHuman;
}
