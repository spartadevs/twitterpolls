package twitter.streaming;

public class KafkaZooKeeperDummy {
    private Producer producer;
    private Consumer consumer;

    public KafkaZooKeeperDummy() {
        producer = new Producer(this);
        consumer = new Consumer();
    }

    public Producer getProducer() {
        return producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }
}
