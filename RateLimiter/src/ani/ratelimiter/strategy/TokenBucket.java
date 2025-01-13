package ani.ratelimiter.strategy;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In case of token bucket algorithm there is a fixed rate at which
 * tokens are added
 * <p>
 * token Rate - Number of tokens added per timeunit(min/secs)
 * <p>
 * Token to add at any given point
 * = Time since the last refill * tokenRate
 */


public class TokenBucket {


    private final int MAX_CAPACITY = 10;

    private final int REFILL_RATE_IN_MILLIS = 10;

    // change this to thread safe implementation
    private long lastRefillTime = System.currentTimeMillis();

    private final Lock lock = new ReentrantLock();

    private AtomicInteger tokens;

    public TokenBucket() {
        this.tokens = new AtomicInteger(MAX_CAPACITY); // Initialise with Atomic Integer with max capacity
    }

    /**
     * Rate of refill is every 10 sec.
     * Max token allowed is till MAX_CAPACITY
     *
     * @return
     */
    public boolean allow() {
        try {
            lock.lock();
            refill();
            if (tokens.get() > 0) {
                tokens.getAndDecrement();
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }


    private void refill() {
        long now = System.currentTimeMillis();
        int tokensToRefill = (int) (now - lastRefillTime) * REFILL_RATE_IN_MILLIS;
        tokens.getAndSet(Math.min(MAX_CAPACITY, tokens.get() + tokensToRefill));


    }


}
