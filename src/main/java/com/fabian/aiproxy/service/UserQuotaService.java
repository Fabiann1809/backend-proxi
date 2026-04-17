package com.fabian.aiproxy.service;

import com.fabian.aiproxy.model.UserQuota;

public interface UserQuotaService {

    UserQuota getOrCreate(String userId);

    void incrementRequestCount(String userId);

    void consumeTokens(String userId, long tokens);
}
