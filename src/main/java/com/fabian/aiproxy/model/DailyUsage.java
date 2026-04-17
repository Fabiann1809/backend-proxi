package com.fabian.aiproxy.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DailyUsage {

    private LocalDate date;
    private long tokensUsed;
    private int requestCount;
}
