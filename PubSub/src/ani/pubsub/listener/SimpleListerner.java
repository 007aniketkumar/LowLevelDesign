package ani.pubsub.listener;

import ani.pubsub.entity.Message;

public class SimpleListerner implements ConsumerListener{

    private final String consumerID;

    public SimpleListerner(String consumerID) {
        this.consumerID = consumerID;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("Message :: " +
                " - Content: " + message.getContent() +
                " is read by: " + consumerID);
    }



}
