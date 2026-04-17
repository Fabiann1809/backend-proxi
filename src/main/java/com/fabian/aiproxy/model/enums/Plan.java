package com.fabian.aiproxy.model.enums;

public enum Plan {

    FREE(10, 50_000),
    PRO(60, 500_000),
    ENTERPRISE(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int requestsPerMinute;
    private final long monthlyTokens;

    Plan(int requestsPerMinute, long monthlyTokens) {
        this.requestsPerMinute = requestsPerMinute;
        this.monthlyTokens = monthlyTokens;
    }

    public int getRequestsPerMinute() {
        return requestsPerMinute;
    }

    public long getMonthlyTokens() {
        return monthlyTokens;
    }
}
