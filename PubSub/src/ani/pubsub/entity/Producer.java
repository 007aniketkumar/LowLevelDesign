package ani.pubsub.entity;

import ani.pubsub.handler.PubSubManager;

public class Producer implements Runnable{

    public Producer(PubSubManager pubSubManager, String topicName, String producerName) {
        this.pubSubManager = pubSubManager;
        this.topicName = topicName;
        this.producerName = producerName;
    }

    private final PubSubManager pubSubManager;
    private final String topicName;
    private final String producerName;


    @Override
    public void run() {
        for(int i=0;i<10;i++){
            String message = producerName + " - Message content: " + i;

            pubSubManager.publishMessage(message ,  topicName, producerName);
        }
    }
}
