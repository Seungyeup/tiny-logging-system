# tiny-logging-system
toy project for mydata logging system


1. zoo-keeper, kafka broker 실행
- bin/kafka-server-start.sh -daemon config/server.properties

### 카프가 동작상태 확인
- bin/kafka-broker-api-versions.sh --bootstrap-server my-kafka:9092


2. kafka topic
kafka dir: /Users/lsy/kafka_for_logging

bin/kafka-topics.sh --create \
--bootstrap-server my-kafka:9092 \
--replication-factor 1 \
--partitions 3 \
--topic shinhan-mydata-log

### 싱글 브로커인 경우 replication-factor 1로 잡고 실행
- 토픽 확인: bin/kafka-topics.sh --list --bootstrap-server my-kafka:9092
- 토픽 삭제: bin/kafka-topics.sh --delete --zookeeper my-kafka:2181 --topic {토픽명}

3. 

curl -L -X POST 'localhost:8083/connectors' \
-H 'Content-Type: application/json' \
--data-raw '{
    "name": "es-sink-connector",
    "config": {
        "connector.class": "com.pipeline.ElasticSearchSinkConnector",
        "topics": "shinhan-mydata-log",
        "es.host": "localhost",
        "es.port": "9200",
        "es.index": "kafka-to-es"
    }
}'