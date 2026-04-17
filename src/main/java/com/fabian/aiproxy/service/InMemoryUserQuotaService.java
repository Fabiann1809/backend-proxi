package com.fabian.aiproxy.service;

import com.fabian.aiproxy.model.DailyUsage;
import com.fabian.aiproxy.model.UserQuota;
import com.fabian.aiproxy.model.enums.Plan;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryUserQuotaService implements UserQuotaService {

    private final ConcurrentHashMap<String, UserQuota> store = new ConcurrentHashMap<>();

    @Override
    public UserQuota getOrCreate(String userId) {
        UserQuota quota = store.computeIfAbsent(userId, this::newQuota);
        resetMinuteWindowIfExpired(quota);
        return quota;
    }

    @Override
    public void incrementRequestCount(String userId) {
        UserQuota quota = getOrCreate(userId);
        quota.setRequestsThisMinute(quota.getRequestsThisMinute() + 1);
    }

    @Override
    public void consumeTokens(String userId, long tokens) {
        UserQuota quota = getOrCreate(userId);
        quota.setTokensUsedThisMonth(quota.getTokensUsedThisMonth() + tokens);
        recordDailyUsage(quota, tokens);
    }

    private UserQuota newQuota(String userId) {
        LocalDateTime now = LocalDateTime.now();
        return UserQuota.builder()
                .userId(userId)
                .plan(Plan.FREE)
                .requestsThisMinute(0)
                .tokensUsedThisMonth(0)
                .minuteWindowStart(now)
                .monthResetDate(now.plusMonths(1))
                .dailyHistory(new ArrayList<>())
                .build();
    }

    private void resetMinuteWindowIfExpired(UserQuota quota) {
        if (quota.getMinuteWindowStart().isBefore(LocalDateTime.now().minusMinutes(1))) {
            quota.setRequestsThisMinute(0);
            quota.setMinuteWindowStart(LocalDateTime.now());
        }
    }

    private void recordDailyUsage(UserQuota quota, long tokens) {
        LocalDate today = LocalDate.now();
        List<DailyUsage> history = quota.getDailyHistory();

        history.stream()
                .filter(d -> d.getDate().equals(today))
                .findFirst()
                .ifPresentOrElse(
                        d -> {
                            d.setTokensUsed(d.getTokensUsed() + tokens);
                            d.setRequestCount(d.getRequestCount() + 1);
                        },
                        () -> {
                            if (history.size() >= 7) {
                                history.remove(0);
                            }
                            history.add(DailyUsage.builder()
                                    .date(today)
                                    .tokensUsed(tokens)
                                    .requestCount(1)
                                    .build());
                        }
                );
    }
}
