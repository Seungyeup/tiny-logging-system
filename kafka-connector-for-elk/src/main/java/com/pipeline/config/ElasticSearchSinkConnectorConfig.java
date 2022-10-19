package com.pipeline.config;


import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import java.util.Map;

public class ElasticSearchSinkConnectorConfig extends AbstractConfig {

    public static final String ES_CLUSTER_HOST="es.host";
    private static final String ES_CLUSTER_HOST_DEFAULT_VALUE = "localhost";
    private static final String ES_CLUSTER_HOST_DOC = "elastic_search_host";

    public static final String ES_CLUSTER_PORT = "es.port";
    private static final String ES_CLUSTER_PORT_DEFAULT_VALUE = "9200";
    private static final String ES_CLUSTER_PORT_DOC = "elastic_search_port";

    public static final String ES_INDEX = "es.index";
    private static final String ES_INDEX_DEFAULT_VALUE = "kafka_connector_index";
    private static final String ES_INDEX_DOC = "elastic_search_index";

    // ConfigDef 인스턴스는 커넥터에서 설정값이 정상적으로 들어왔는지 검증하기 위해 사용함.
    public static ConfigDef CONFIG = new ConfigDef()
            .define(
                    ES_CLUSTER_HOST, ConfigDef.Type.STRING,
                    ES_CLUSTER_HOST_DEFAULT_VALUE,
                    ConfigDef.Importance.HIGH, ES_CLUSTER_HOST_DOC )
            .define(
                    ES_CLUSTER_PORT, ConfigDef.Type.INT,
                    ES_CLUSTER_PORT_DEFAULT_VALUE,
                    ConfigDef.Importance.HIGH, ES_CLUSTER_PORT_DOC )
            .define(
                    ES_INDEX, ConfigDef.Type.STRING,
                    ES_INDEX_DEFAULT_VALUE,
                    ConfigDef.Importance.HIGH, ES_INDEX_DOC );

    public ElasticSearchSinkConnectorConfig( Map<String, String> props ) {
        super(CONFIG, props);
    }

}
