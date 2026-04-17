package com.fabian.aiproxy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DailyUsageResponse {

    private LocalDate date;
    private long tokensUsed;
    private int requestCount;
}
