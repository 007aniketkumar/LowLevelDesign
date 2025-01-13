package ani.pubsub.listener;

import ani.pubsub.entity.Message;

public interface ConsumerListener {
    void onMessage(Message message) ; //called when a message is published ,
    //each consumer has a listener.


}
