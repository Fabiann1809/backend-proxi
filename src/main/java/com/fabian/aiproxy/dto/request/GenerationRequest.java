package com.fabian.aiproxy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerationRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String prompt;
}
