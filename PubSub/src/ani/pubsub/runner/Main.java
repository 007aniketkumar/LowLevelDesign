package ani.pubsub.runner;

import ani.pubsub.entity.Producer;
import ani.pubsub.handler.PubSubManager;
import ani.pubsub.listener.SimpleListerner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *  This is a good problem to understand : multiple publishers and consumers
 *
 *
 */


public class Main {
    public static void main(String[] args) {


        PubSubManager pubSubManager = new PubSubManager();
        pubSubManager.createTopic("topic1");

        //add consumers
        pubSubManager.subscribeTopic("ConsumerID1" , "topic1", new SimpleListerner("ConsumerID1"));
        pubSubManager.subscribeTopic("ConsumerID2" , "topic1", new SimpleListerner("ConsumerID2"));







    // concurrently adding messages

        ExecutorService executors = Executors.newFixedThreadPool(2);
        executors.submit(new Producer(pubSubManager,"topic1","producer1"));
        executors.submit(new Producer(pubSubManager,"topic1","producer2"));

        executors.shutdown();

        //pubSubManager.shutDown();



    }


}