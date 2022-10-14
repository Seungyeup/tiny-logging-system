package com.pipeline;

import com.google.gson.Gson;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestApiKafkaController {
    private final Logger logger = LoggerFactory.getLogger(RestApiKafkaProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate; // kafka template 인스턴스를 생성. 이때 Bxm-Framework 에서는 해당 기능을 개발해야 할 수도 있다.

    public RestApiKafkaController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/api/select")
    public void selectLog(
        @RequestHeader("user-agent") String userAgentName,
        @RequestParam(value = "log_type") String logType,
        @RequestParam(value = "cus_no") String cusNo){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        Date now = new Date();
        Gson gson = new Gson();
        UserEventVO userEventVO = new UserEventVO(sdfDate.format(now), userAgentName, logType, cusNo );

        String jsonEventLog = gson.toJson(userEventVO);
        kafkaTemplate.send("shinhan-mydata-log", jsonEventLog).addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                logger.error(ex.getMessage(), ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                logger.info(result.toString());
            }
        });
    }
}
