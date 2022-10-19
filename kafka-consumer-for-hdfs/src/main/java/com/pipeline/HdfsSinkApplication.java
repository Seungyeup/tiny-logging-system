package com.pipeline;

import com.pipeline.consumer.ConsumerWorker;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 컨슈머 Thread 들을 실행
public class HdfsSinkApplication {

    private final static Logger logger = LoggerFactory.getLogger(HdfsSinkApplication.class);

    // 카프카 클러스터 INFO
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092";        // 서버
    private final static String TOPIC_NAME = "shinhan-mydata-log";          // 토픽
    private final static String GROUP_ID = "log-hdfs-save-consummer-group"; // 컨슈머그룹

    // 생성할 Thread 개수를 변수로 선언. 나중에 파티션이 늘어나는 경우 -> 이에 맞추어 컨슈머 스레드를 동적으로 운영한다.
    private final static int CONSUMER_COUNT = 3;
    private final static List<ConsumerWorker> workers = new ArrayList<>();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutdownThread()); // 안전한 컨슈머의 종료를 위해 셧다운 훅을 선언한다.

        // 컨슈머 설정. JSON 타입으로 받는 메세지 키, 메세지 값 등은 String 타입으로 역직렬화 시킨다.
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // 컨슈머 스레드를 관리하기 위한 스레드 풀
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 컨슈머 스레드를 CONSUMER_COUNT만큼 선언한다.
        for ( int i=0; i<CONSUMER_COUNT; i++){
            workers.add(new ConsumerWorker(configs, TOPIC_NAME, i));
        }
        workers.forEach(executorService::execute);

    }

    static class ShutdownThread extends Thread {
        public void run() {
            logger.info("Shutdown hook");
            workers.forEach(ConsumerWorker::stopAndMakeup);
        }
    }

}
