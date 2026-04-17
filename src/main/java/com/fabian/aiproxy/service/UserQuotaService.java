package com.fabian.aiproxy.service;

import com.fabian.aiproxy.dto.response.DailyUsageResponse;
import com.fabian.aiproxy.dto.response.QuotaStatusResponse;
import com.fabian.aiproxy.model.UserQuota;
import com.fabian.aiproxy.model.enums.Plan;

import java.util.List;

public interface UserQuotaService {

    UserQuota getOrCreate(String userId);

    void incrementRequests(String userId);

    void deductTokens(String userId, long tokens);

    QuotaStatusResponse getStatus(String userId);

    List<DailyUsageResponse> getHistory(String userId);

    void upgradePlan(String userId, Plan newPlan);

    void resetMinuteCounters();

    void resetMonthlyQuotas();
}
