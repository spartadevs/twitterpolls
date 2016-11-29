package twitter.streaming;

import java.io.IOException;

public class Producer {
    private static org.apache.kafka.clients.producer.KafkaProducer __producer = null;

    static {
        if (__producer == null) {
//            __producer = new org.apache.kafka.clients.producer.KafkaProducer(PropertyReader.get());
        }
    }

    public static void main(String[] args) throws IOException {
        (new Producer()).send("topic", "abc");
    }

    public void send(String topic, String message) {
//        __producer.send(new ProducerRecord<String, String>(
//                topic,
//                message));
//        __producer.flush();
        Consumer.getInstance().consume(topic, message);
        System.out.println("Sent: " + topic + " : " + message);
    }
}
