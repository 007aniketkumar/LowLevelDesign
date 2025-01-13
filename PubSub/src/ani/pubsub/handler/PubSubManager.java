package ani.pubsub.handler;


import ani.pubsub.entity.Consumer;
import ani.pubsub.entity.Message;
import ani.pubsub.entity.Topic;
import ani.pubsub.listener.ConsumerListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This can be thought of as wrapper on topic class
 * <p>
 * This can also be exposed as a service
 * with every method as an endpoint exposed.
 */


public class PubSubManager {
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();

    /**
     * @param topicName
     */

    public void createTopic(String topicName) {
        topics.putIfAbsent(topicName, new Topic(topicName));
    }

    /**
     * publish message API
     */

    public void publishMessage(String content, String topicName, String producerName) {
        //create a message object and get the topic object from the map and call the publish method
        Topic topic = topics.get(topicName);
        if (topic == null) {
            System.out.println("Topic name does not exist");
            throw new RuntimeException("Invalid topic name exception");
        } else {
            long messageId = topic.getNextOffset();
            System.out.println("Message  -->  :: " + messageId + ":: published successfuly to the topic --> : " + topicName + " by producer :: -->" + producerName);
            topic.addMessage(new Message(messageId, content));
        }


    }


    /**
     * Subscribe to a topic - add a consumer to a topic
     */


    public void subscribeTopic(String consumerId, String topicName, ConsumerListener consumerListener) {
        //create a message object and get the topic object from the map and call the publish method
        Topic topic = topics.get(topicName);
        if (topic == null) {
            System.out.println("Topic name does not exist");
            throw new RuntimeException("Invalid topic name exception");
        } else {
            topic.addConsumers(new Consumer(consumerId, consumerListener));
        }
    }


    /**
     * Unsubscribe from a topic
     */

    public void unSubscribeTopic(String consumerId, String topicName) {
        //create a message object and get the topic object from the map and call the publish method
        Topic topic = topics.get(topicName);
        if (topic == null) {
            System.out.println("Topic name does not exist");
            throw new RuntimeException("Invalid topic name exception");
        } else {
            topic.removeConsumer(consumerId);
        }
    }


    /**
     * clean up the resources and close the exectuors
     */

    public void shutDown() {

        //iterate over the map and clean up the executors on each topics.

        for (Map.Entry<String, Topic> e : topics.entrySet()) {
            e.getValue().shutDown();
        }
    }

}
