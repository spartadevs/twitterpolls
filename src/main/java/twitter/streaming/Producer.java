package twitter.streaming;

import java.io.IOException;

import com.google.common.io.Resources;

public class Producer {
    KafkaZooKeeperDummy zooKeeper;

    Producer(KafkaZooKeeperDummy zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void send(String topic, String message) {
        zooKeeper.getConsumer().consume(topic, message);
        System.out.println("Sent: " + topic + " : " + message);
    }
}
