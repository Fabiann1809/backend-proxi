package com.fabian.aiproxy.dto.response;

import com.fabian.aiproxy.model.enums.Plan;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuotaStatusResponse {

    private String userId;
    private Plan plan;
    private long tokensUsed;
    private long tokensRemaining;
    private long totalTokens;
    private int requestsThisMinute;
    private int requestsLimit;
    private LocalDateTime resetDate;
    private LocalDateTime minuteResetAt;
}
