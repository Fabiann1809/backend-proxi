package com.fabian.aiproxy.service.proxy;

import com.fabian.aiproxy.dto.request.GenerationRequest;
import com.fabian.aiproxy.dto.response.GenerationResponse;
import com.fabian.aiproxy.model.UserQuota;
import com.fabian.aiproxy.service.AIGenerationService;
import com.fabian.aiproxy.service.QuotaExceededException;
import com.fabian.aiproxy.service.UserQuotaService;

public class QuotaProxyService implements AIGenerationService {

    private final AIGenerationService next;
    private final UserQuotaService quotaService;

    public QuotaProxyService(AIGenerationService next, UserQuotaService quotaService) {
        this.next = next;
        this.quotaService = quotaService;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) throws Exception {
        GenerationResponse response = next.generate(request);

        UserQuota quota = quotaService.getOrCreate(request.getUserId());
        long tokensAfter = quota.getTokensUsedThisMonth() + response.getTokensUsed();

        if (tokensAfter > quota.getPlan().getMonthlyTokens()) {
            long remaining = quota.getPlan().getMonthlyTokens() - quota.getTokensUsedThisMonth();
            throw new QuotaExceededException(
                    "Monthly token quota exceeded for plan " + quota.getPlan() +
                    ". Remaining tokens: " + remaining +
                    ". Resets on: " + quota.getMonthResetDate()
            );
        }

        quotaService.consumeTokens(request.getUserId(), response.getTokensUsed());

        return response;
    }
}
