package ani.ratelimiter.strategy;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 *
 *
 * In case of sliding window ==> Deque
 *
 * add new timestamps from end
 * remove from the front
 *
 *   deque
 *
 *      remove                  add
 *      ------------ times ----------
 *
 *
 *
 *
 *
 */

public class SlidingWindow implements RateLimiterStrategy{

    AtomicLong credit = new AtomicLong(0L); //Keeping a global variable to start
    //with

    Deque<Long> timestamps;

    private final long WINDOW_SIZE_MILLIS=100;
    private final int MAX_REQUESTS_IN_A_WINDOW=100; // To be initialised by constructor

    @Override
    public synchronized  boolean isAllowed() {

        timestamps = new LinkedList<>();
        long out_of_window = (System.currentTimeMillis()-WINDOW_SIZE_MILLIS);
        while(timestamps.peekFirst() < out_of_window){
            timestamps.pollFirst();
        }
        //check the size of queue now to see if the request is to be allowed

        if(timestamps.size()< MAX_REQUESTS_IN_A_WINDOW){
            timestamps.addLast(System.currentTimeMillis());
            return true;

        }


        return false;
    }


     //sliding window



}
