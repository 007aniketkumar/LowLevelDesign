package ani.ratelimiter.strategy;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Check the regular rate at which tokens are removed,
 * if the list is empty implies that requests can be accepted.
 * <p>
 * Same code as Token bucket , just handle
 * it the other way out
 * ie remove
 * at fix rate
 * if the bucket is full implies , it cannot take in any more request.
 */
public class LeakyBucket {

    private final int TOKEN_LEAK_RATE_PER_MILLIS = 1;

    private long LAST_LEAK_TIME_IN_MILLIS = System.currentTimeMillis();

    //Implies that all the requests can be accepted.
    private AtomicInteger tokens = new AtomicInteger(0);

    private final int MAX_CAPACITY = 100;

    private final ReentrantLock lock = new ReentrantLock();


    public boolean isAllowed() {
        lock.lock();
        try {

            release();
            if (MAX_CAPACITY == tokens.get()) { // if the bucket is full , dont allow any request
                return false;
            } else {
                tokens.incrementAndGet(); //handle the current request
                return true;
            }
        } finally {
            lock.unlock();
        }
    }


    private void release() {
        long elapsedTime = System.currentTimeMillis() - LAST_LEAK_TIME_IN_MILLIS;
        if (elapsedTime > 0) {
            //release the bucket
            int tokens_to_release = (int) elapsedTime * TOKEN_LEAK_RATE_PER_MILLIS;
            LAST_LEAK_TIME_IN_MILLIS = System.currentTimeMillis();
            tokens.set(Math.max(0, tokens.get() - tokens_to_release));
        }
    }


}
