package com.fabian.aiproxy.config;

import com.fabian.aiproxy.service.AIGenerationService;
import com.fabian.aiproxy.service.MockAIGenerationService;
import com.fabian.aiproxy.service.UserQuotaService;
import com.fabian.aiproxy.service.proxy.QuotaProxyService;
import com.fabian.aiproxy.service.proxy.RateLimitProxyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AIServiceConfig {

    // Chain: RateLimitProxy → QuotaProxy → MockAIGenerationService
    @Bean
    @Primary
    public AIGenerationService aiGenerationService(
            MockAIGenerationService mockService,
            UserQuotaService quotaService
    ) {
        AIGenerationService withQuota = new QuotaProxyService(mockService, quotaService);
        return new RateLimitProxyService(withQuota, quotaService);
    }
}
