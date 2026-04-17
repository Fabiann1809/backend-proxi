package com.fabian.aiproxy.service;

import com.fabian.aiproxy.dto.response.DailyUsageResponse;
import com.fabian.aiproxy.dto.response.QuotaStatusResponse;
import com.fabian.aiproxy.model.DailyUsage;
import com.fabian.aiproxy.model.UserQuota;
import com.fabian.aiproxy.model.enums.Plan;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class InMemoryUserQuotaService implements UserQuotaService {

    private final ConcurrentHashMap<String, UserQuota> store = new ConcurrentHashMap<>();

    @Override
    public UserQuota getOrCreate(String userId) {
        return store.computeIfAbsent(userId, this::newQuota);
    }

    @Override
    public void incrementRequests(String userId) {
        UserQuota quota = getOrCreate(userId);
        quota.setRequestsThisMinute(quota.getRequestsThisMinute() + 1);
    }

    @Override
    public void deductTokens(String userId, long tokens) {
        UserQuota quota = getOrCreate(userId);
        quota.setTokensUsedThisMonth(quota.getTokensUsedThisMonth() + tokens);
        recordDailyUsage(quota, tokens);
    }

    @Override
    public QuotaStatusResponse getStatus(String userId) {
        UserQuota quota = getOrCreate(userId);
        long total = quota.getPlan().getMonthlyTokens();
        long used = quota.getTokensUsedThisMonth();
        long remaining = Math.max(total - used, 0);

        return QuotaStatusResponse.builder()
                .userId(userId)
                .plan(quota.getPlan())
                .tokensUsed(used)
                .tokensRemaining(remaining)
                .totalTokens(total)
                .requestsThisMinute(quota.getRequestsThisMinute())
                .requestsLimit(quota.getPlan().getRequestsPerMinute())
                .resetDate(quota.getMonthResetDate())
                .minuteResetAt(quota.getMinuteWindowStart().plusMinutes(1))
                .build();
    }

    @Override
    public List<DailyUsageResponse> getHistory(String userId) {
        UserQuota quota = getOrCreate(userId);
        return quota.getDailyHistory().stream()
                .map(d -> DailyUsageResponse.builder()
                        .date(d.getDate())
                        .tokensUsed(d.getTokensUsed())
                        .requestCount(d.getRequestCount())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void upgradePlan(String userId, Plan newPlan) {
        UserQuota quota = getOrCreate(userId);
        quota.setPlan(newPlan);
    }

    @Override
    public void resetMinuteCounters() {
        store.values().forEach(quota -> {
            quota.setRequestsThisMinute(0);
            quota.setMinuteWindowStart(LocalDateTime.now());
        });
    }

    @Override
    public void resetMonthlyQuotas() {
        LocalDateTime now = LocalDateTime.now();
        if (now.getDayOfMonth() != 1) return;

        store.values().forEach(quota -> {
            quota.setTokensUsedThisMonth(0);
            quota.setMonthResetDate(now.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0));
        });
    }

    private UserQuota newQuota(String userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMonthFirst = now.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        return UserQuota.builder()
                .userId(userId)
                .plan(Plan.FREE)
                .requestsThisMinute(0)
                .tokensUsedThisMonth(0)
                .minuteWindowStart(now)
                .monthResetDate(nextMonthFirst)
                .dailyHistory(new ArrayList<>())
                .build();
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
