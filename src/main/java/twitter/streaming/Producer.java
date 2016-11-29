package twitter.streaming;

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Producer {
    private static org.apache.kafka.clients.producer.KafkaProducer __producer = null;

    static {
        if (__producer == null) {
            try (InputStream props = Resources.getResource("kafka.producer.props").openStream()) {
                Properties properties = new Properties();
                properties.load(props);
                __producer = new org.apache.kafka.clients.producer.KafkaProducer(properties);
            } catch (Exception e) {
                // TODO: 11/27/2016 handle properly
                System.out.println("------------------------- check this");
            }
        }
    }


    public void send(String topic, String message) {
//        __producer.send(new ProducerRecord<String, String>(
//                topic,
//                message));
//        __producer.flush();

        System.out.println("Sent: " + topic + " : " + message);
    }


    public static void main(String[] args) throws IOException {
        (new Producer()).send("topic", "abc");
    }
}
