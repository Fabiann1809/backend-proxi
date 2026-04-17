package com.fabian.aiproxy.scheduler;

import com.fabian.aiproxy.service.UserQuotaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuotaScheduler {

    private final UserQuotaService quotaService;

    public QuotaScheduler(UserQuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @Scheduled(fixedRate = 60000)
    public void resetMinuteCounters() {
        quotaService.resetMinuteCounters();
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void resetMonthlyQuotas() {
        quotaService.resetMonthlyQuotas();
    }
}
