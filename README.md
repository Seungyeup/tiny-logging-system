# tiny-logging-system
toy project for mydata logging system



### 1. 하둡 옵션값 설정, 실행

1-1. Hadoop fs.defaultFS 옵션값 변경
    - core-site.xml
    hadoop version : hadoop version
    core-site.xml location : /usr/local/Cellar/hadoop/3.3.4/libexec/etc/hadoop

1-2. hdfs-site.xml 설정
    - (https://blog.voidmainvoid.net/367)

1-3. Hadoop 실행
    - /usr/local/Cellar/hadoop/3.3.4/libexec/sbin/start-dfs.sh
    - (https://key4920.github.io/docs/bigdata_platform/Hadoop/hadoop_install/)

https://medium.com/beeranddiapers/installing-hadoop-on-mac-a9a3649dbc4d


(permission error의 경우, 클러스터를 구성하지 않고 독립 실행 모드로 실행해서 그렇다)

```bash
# ssh key 생성
$ ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa

# 허가된 key 로 등록
$ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```

1-4. Hadoop 동작 확인
    - jps로 상태확인



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



### 4. 동작상태 확인

1. 웹페이지.html open
2. Restful producer 실행
3. Hadoop 적재 컨슈머 실행
    - hadoop hdfs 실행 /usr/local/Cellar/hadoop/3.3.4/libexec/sbin/start-all.sh
    - 적재 컨슈머 실행
4. 분산모드 카프카 커넥트 실행
    - es 싱크 커넥터 jar 파일 생성, jar 파일 이동
        - from : /Users/lsy/Desktop/tiny-logging-system/kafka-connector-for-elk/build/libs/pipeline-0.0.2-SNAPSHOT.jar 
        - to : local kafka plugins dir (생성한 폴더)
    - 분산 모드 커넥트 설정 파일 수정
        - config/connect-distributed.properties 파일 수정한다.
    - 분산 모드 커넥트 실행
        - bin/connect-distributed.sh config/connect-distributed.properties
    - 분산 모드 커넥트 실행확인
        - `curl http://localhost:8083/connector-plugins`
        - ~ 말고, /Users/lsy/kafka_for_logging 처럼 루트부터 경로 주자. ES 커넥터 추가 안되서 삽질 한참 함
        [shutdown] : ps aux | grep ConnectDistributed
        (https://docs.confluent.io/kafka-connectors/self-managed/userguide.html#shutting-down-kconnect-long)
    - 엘라스틱 서치 싱크 커넥터 실행

    ```json
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
    ```
    
    - 커넥터 실행 확인
        - curl -X GET http://localhost:8083/connectors
5. elasticsearch 실행
    - elasticsearch