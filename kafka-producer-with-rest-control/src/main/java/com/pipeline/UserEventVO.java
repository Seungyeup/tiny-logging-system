package com.pipeline;

public class UserEventVO {

    // 시간값을 알고 있으면 컨슈머에서 병렬처리가 되더라도 최종 적재된 데이터의 timestamp 를 기준으로 정렬할 수 있다.
    private String timestamp;
    private String userAgent;
    private String cus_no;
    private String log_type;

    public UserEventVO(String timestamp, String userAgent, String cus_no, String log_type){
        this.timestamp = timestamp;
        this.timestamp = userAgent;
        this.timestamp = cus_no;
        this.timestamp = log_type;
    }

}
