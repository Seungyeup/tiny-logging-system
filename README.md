# tiny-logging-system
toy project for mydata logging system

1. kafka topic
kafka dir: /Users/lsy/kafka_for_logging

bin/kafka-topics.sh --create \
--bootstrap-server my-kafka:9092 \
--replication-factor 1 \
--partitions 3 \
--topic shinhan-mydata-log

### 싱글 브로커인 경우 replication-factor 1로 잡고 실행
- 토픽 확인: bin/kafka-topics.sh --list --bootstrap-server my-kafka:9092
- 토픽 삭제: bin/kafka-topics.sh --delete --zookeeper my-kafka:2181 --topic {토픽명}



