package com.fabian.aiproxy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GenerationResponse {

    private String userId;
    private String generatedText;
    private int tokensUsed;
    private long processingTimeMs;
    private LocalDateTime timestamp;
}
