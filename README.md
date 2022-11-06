# tiny-logging-system
toy project for mydata logging system

### 1. 옵션값 설정
1-1. Hadoop fs.defaultFS 옵션값 변경
    - core-site.xml
    - 
    hadoop version : hadoop version
    core-site.xml location : /usr/local/Cellar/hadoop/3.3.4/libexec/etc/hadoop


### 2. 카프가 동작상태 확인
kafka local dir: /Users/lsy/kafka_for_logging
kafka remote dir: ~/kafka_2.12-2.5.0

2-1.  zoo-keeper, kafka broker 실행
- 카프카 브로커 실행: [kafka-broker] bin/kafka-server-start.sh -daemon config/server.properties
- 카프카 API 버전, 목록 확인: [local] bin/kafka-broker-api-versions.sh --bootstrap-server my-kafka:9092


### 3. 카프카 프로커에 토픽 생성

3-1. 카프카 토픽 생성: [local]
bin/kafka-topics.sh --create \
--bootstrap-server my-kafka:9092 \
--replication-factor 1 \
--partitions 3 \
--topic shinhan-mydata-log

#### 싱글 브로커인 경우 replication-factor 1로 잡고 실행
- 토픽 확인: [local] bin/kafka-topics.sh --list --bootstrap-server my-kafka:9092
    - (예시)
    - __consumer_offsets
    - connect-configs
    - connect-offsets
    - connect-status
    - shinhan-mydata-log
- 토픽 삭제: [local] bin/kafka-topics.sh --delete --zookeeper my-kafka:2181 --topic {토픽명}


### 4. test es-sink-connector

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
