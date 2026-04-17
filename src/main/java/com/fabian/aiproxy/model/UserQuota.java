package com.fabian.aiproxy.model;

import com.fabian.aiproxy.model.enums.Plan;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserQuota {

    private String userId;
    private Plan plan;
    private int requestsThisMinute;
    private long tokensUsedThisMonth;
    private LocalDateTime minuteWindowStart;
    private LocalDateTime monthResetDate;
    private List<DailyUsage> dailyHistory;
}
