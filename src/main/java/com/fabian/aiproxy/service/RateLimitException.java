package com.fabian.aiproxy.service;

public class RateLimitException extends RuntimeException {

    private final int retryAfterSeconds;

    public RateLimitException(int retryAfterSeconds) {
        super("Rate limit exceeded. Retry after " + retryAfterSeconds + " seconds.");
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
