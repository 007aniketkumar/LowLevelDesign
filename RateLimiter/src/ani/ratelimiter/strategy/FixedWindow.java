package ani.ratelimiter.strategy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * fixed Window time --say 1 hour->
 *
 * last window update Time
 *  total requests  allowed in a window
 *
 *
 *
 *
 */

public class FixedWindow implements RateLimiterStrategy{

    private final int MAX_REQUEST_ALLOWED_IN_WINDOW = 100;
    private final long WINDOW_SIZE_IN_SECS=100;
    private AtomicInteger requestsProcessed=new AtomicInteger(0);
    private long window_start_time =0L;

    @Override
    public boolean isAllowed() {
        long elapsedTimeInSecs = (System.currentTimeMillis() -  window_start_time)/1000;
        if(elapsedTimeInSecs< WINDOW_SIZE_IN_SECS){
          if(requestsProcessed.get() <MAX_REQUEST_ALLOWED_IN_WINDOW)  {
              requestsProcessed.incrementAndGet();
              return true;
          }else{
              return false;
          }
        } else{
            //reset the request processed and widnow start time
            window_start_time= System.currentTimeMillis();
            requestsProcessed.set(1); // first request
            return true;
        }
    }
}
