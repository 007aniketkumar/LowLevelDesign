package ani.pubsub.entity;


//each consumer has a consumer listener.

import ani.pubsub.listener.ConsumerListener;

public class Consumer {
    private final String id; //consumer id
    private final ConsumerListener listener;

    public Consumer(String id, ConsumerListener listener) {
        this.id = id;
        this.listener = listener;
    }

    public String getId() {
        return id;
    }

    public ConsumerListener getListener() {
        return listener;
    }
}
