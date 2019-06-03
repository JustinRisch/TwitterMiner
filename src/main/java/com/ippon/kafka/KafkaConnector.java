package com.ippon.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.pojo.TweetPojo;
import com.ippon.pojo.UserPojo;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;

import java.net.InetAddress;
import java.util.Properties;

public class KafkaConnector {
    String topic;

    public KafkaConnector(String topic) {
        this.topic = topic;
    }

    public static void main(String[] args) {
        TweetPojo t = new TweetPojo();
        UserPojo u = new UserPojo();
        u.setScreenName("test");
        t.setUser(u);
        t.setText("This is a test");
        KafkaConnector kc = new KafkaConnector("test");
        kc.dropTweet(t);
    }


    Properties props = new Properties();
    {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ":9092");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        } catch
        (Exception e) {
            e.printStackTrace();
        }
    }


    public void dropTweet(TweetPojo tweet) {
        try {
            String json = new ObjectMapper().writeValueAsString(tweet);
            System.out.println("Publishing: " + json);
            KafkaProducer<String, String> producer = new KafkaProducer<>(props);
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    topic, tweet.getId()+"", json);
            producer.send(record);
            producer.close();
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
        }
    }
}
