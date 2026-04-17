package com.fabian.aiproxy.service.proxy;

import com.fabian.aiproxy.dto.request.GenerationRequest;
import com.fabian.aiproxy.dto.response.GenerationResponse;
import com.fabian.aiproxy.model.UserQuota;
import com.fabian.aiproxy.service.AIGenerationService;
import com.fabian.aiproxy.service.RateLimitException;
import com.fabian.aiproxy.service.UserQuotaService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RateLimitProxyService implements AIGenerationService {

    private final AIGenerationService next;
    private final UserQuotaService quotaService;

    public RateLimitProxyService(AIGenerationService next, UserQuotaService quotaService) {
        this.next = next;
        this.quotaService = quotaService;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) throws Exception {
        UserQuota quota = quotaService.getOrCreate(request.getUserId());
        int limit = quota.getPlan().getRequestsPerMinute();

        if (quota.getRequestsThisMinute() >= limit) {
            long secondsUntilReset = ChronoUnit.SECONDS.between(
                    LocalDateTime.now(),
                    quota.getMinuteWindowStart().plusMinutes(1)
            );
            throw new RateLimitException((int) Math.max(secondsUntilReset, 1));
        }

        quotaService.incrementRequestCount(request.getUserId());

        return next.generate(request);
    }
}
