package com.example.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    //自定义topic
    public static final String TOPIC_TEST = "topic.test";

    public static final String TOPIC_TEST2 = "topic2";
    public static final String TOPIC_GROUP1 = "topic.group1";

    public void send(Object obj) {
        String obj2String = JSON.toJSONString(obj);
        log.info("准备发送消息为：{"+obj2String+"}", obj2String);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC_TEST, obj2String);
        //回调函数
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                log.info(TOPIC_TEST + " - 生产者 发送消息失败：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //成功的处理
                log.info(TOPIC_TEST + " - 生产者 发送消息成功：" + obj2String);
            }
        });
    }

    public void send(Object msg,Object obj){
        String ObjString = JSON.toJSONString(obj);
        String MsgString = JSON.toJSONString(msg);


        List<String> arrayList = new ArrayList<>();

        arrayList.add(ObjString);
        arrayList.add(MsgString);

        log.info("准备发送消息为：{"+arrayList+"}", arrayList);

        String ListJosnString = JSON.toJSONString(arrayList);

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC_TEST2, ListJosnString);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info(TOPIC_TEST2 + " - 生产者 发送消息失败: " + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                log.info(TOPIC_TEST2 + " - 生产者 发送消息成功: " + stringObjectSendResult.toString());
            }
        });

    }

}

