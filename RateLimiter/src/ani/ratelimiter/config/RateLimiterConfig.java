package ani.ratelimiter.config;

import java.time.Instant;

/**
 *
 */


public class RateLimiterConfig {

    private String key;

    private int bucketSize;
    private int tokenRefillRate;

    private int windowSize;


    private Instant lastUpdatedTime;

    public RateLimiterConfig(String key, int bucketSize, int tokenRefillRate, int windowSize, long lastUpdatedTime) {
        this.key = key;
        this.bucketSize = bucketSize;
        this.tokenRefillRate = tokenRefillRate;
        this.windowSize = windowSize;
        this.lastUpdatedTime = Instant.now();
    }
}
