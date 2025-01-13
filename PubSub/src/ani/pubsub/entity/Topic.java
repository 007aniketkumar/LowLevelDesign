package ani.pubsub.entity;

import ani.pubsub.listener.ConsumerListener;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Does most of the work in this system
 * <p>
 * Each topic maps to a queue
 * Topic keeps track of each consumer offset
 * Maintains the consumers - subscribers
 * <p>
 * <p>
 * responsible for -
 * adding message to the queue
 * --> once message is added notify the consumers
 * <p>
 * add Consumer
 * remove consumer
 * <p>
 * notify consumer
 * <p>
 * consumerMessage
 */


public class Topic {

    public String getTopicName() {
        return topicName;
    }


    private final String topicName;

    public CopyOnWriteArrayList<Message> getMessages() {
        return messages;
    }

    private final CopyOnWriteArrayList<Message> messages = new CopyOnWriteArrayList<>();
    private final Map<String, Integer> consumerOffsets = new ConcurrentHashMap<>();
    private final Map<String, Consumer> consumers = new ConcurrentHashMap<>(); //what is this used for ?
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicLong currentOffset = new AtomicLong(0L);
    private final ExecutorService deliveryExecutor = Executors.newCachedThreadPool();


    public Topic(String topicName) {
        this.topicName = topicName;
    }


    /**
     * Add consumers
     */

    public void addConsumers(Consumer consumer) {
        lock.lock();
        try {
            consumers.put(consumer.getId(), consumer);
            consumerOffsets.putIfAbsent(consumer.getId(), 0); //just adding the consumer and initialising its offset
        } finally {
            lock.unlock();
        }
    }

    /**
     * Remove consumer
     */

    public void removeConsumer(String consumerId) {
        lock.lock();
        try {
            consumers.remove(consumerId);
            consumerOffsets.remove(consumerId);
        } finally {
            lock.unlock(); //release the lock
        }
    }


    /**
     * ADD MESSAGE TO THE QUEUE based on message
     */

    public void addMessage(Message message) {
        //thread safe
        lock.lock();
        try {
            messages.add(message);
            // the notify the customers by other thread.
            deliveryExecutor.submit(this::notifyConsumers);
        } finally {
            lock.unlock();
        }
    }


    /**
     * based on new message notify all the consumers
     */

    public void notifyConsumers() {
        //iterate over the map
        for (Map.Entry<String, Consumer> entry : consumers.entrySet()) {
            String consumerId = entry.getKey();
            Consumer consumer = entry.getValue();
            consumeMessages(consumerId, consumer);
        }
    }


    /**
     * check the offset of the consumer and then check how many messages need to be consumed and run the
     * consumer
     */

    public void consumeMessages(String consumerId, Consumer consumer) {
        lock.lock();
        List<Message> newMessages = new ArrayList<>();

        try {
            int offset = consumerOffsets.getOrDefault(consumerId, 0);

            for (int i = offset; i < messages.size(); i++) {
                consumerOffsets.put(consumerId, i + 1);
                newMessages.add(messages.get(i));


            }
        } finally {
            lock.unlock();
        }

        for (int j = 0; j < newMessages.size(); j++) {
            consumer.getListener().onMessage(newMessages.get(j));

        }
    }


    public long getNextOffset() {
        return currentOffset.getAndIncrement();
    }


    public void shutDown() {
        deliveryExecutor.shutdown();
    }


}
