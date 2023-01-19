package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SimpleProducer {
    private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);

    private final static String TOPIC_NAME = "simple-producer-test"; // producer 가 전송하고자 하는 토픽
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092"; // 전송하고자 하는 호스트 IP BOOTSTRAP: 한번 알면 알아서 진행되는 일련의 과정!

    public static void main(String[] args) {

        // Properties 객체는 key-value 에 String 만 사용가능하다.
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS); // 필수옵션들은 받드시 선언해야 한다.
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // StringSerializer: String 객체를 직렬화 - for key
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // StringSerializer: String 객체를 직렬화 - for value

        // KafkaProducer 는 config 값만 받는 것은 아니고 오버로딩 형태로 계속 파라미터를 추가할 수 있음
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs); // KafkaProducer 의 생성 파라미터를 추가하여 인스턴스 생성

        String messageValue = "testMessage"; // 메세지값 선언

        // ProducerRecord 역시 여러개의 생성자 파라미터를 오버로딩 형태로 가지며, 현재 토믹 이름, 메세지 값만 넣었다.
        ProducerRecord<String,String> record = new ProducerRecord<>(TOPIC_NAME, messageValue); // 카프카 브로커로 데이터를 보내기 위해 ProducerRecord 를 생성한다.

        // kafka 에서 send 는 즉각적인 전송이 아니라 파라미터로 들어간 record 를 프로듀서 내부에 가지고 있다가 배치 형태로 묶어서 브로커에 전송하며,
        // 이런 방식을 '배치 전송' 이라고 부른다.
        producer.send(record);

        logger.info("{}", record);

        producer.flush(); // 프로듀서 내부 버퍼에 가지고 있던 레코드 배치를 브로커로 전송한다.
        producer.close();
    }
}
