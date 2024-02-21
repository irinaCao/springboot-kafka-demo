package com.example.consumer;

import com.example.producer.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = KafkaProducer.TOPIC_TEST, groupId = KafkaProducer.TOPIC_GROUP1)
    public void topic_test(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            String msgString = msg.toString();
            System.out.println(msgString);
            log.info("topic.group1 消费了： Topic:" + topic + ",Message:" + msg);
            ack.acknowledge();
        }
    }

    @KafkaListener(topics = KafkaProducer.TOPIC_TEST, groupId = KafkaProducer.TOPIC_GROUP2)
    public void topic_test1(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            log.info("topic.group2 消费了： Topic:" + topic + ",Message:" + msg);
            ack.acknowledge();
        }
    }

    @KafkaListener(topics = KafkaProducer.TOPIC_TEST2, groupId = KafkaProducer.TOPIC_GROUP1)
    public void topic_test2(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        //ConsumerRecord<?, ?> record 这是接收到的消息记录，它包含了从Kafka中消费到的消息的详细信息，例如消息的key、value、分区、偏移量等。
        //Acknowledgment ack 这是用于手动提交消费者偏移量的对象。在消息成功处理后，通过调用ack.acknowledge()来通知Kafka将该消息的偏移量提交给服务器
        //@Header(KafkaHeaders.RECEIVED_TOPIC) String topic 这是通过@Header注解指定的消费者方法的参数，用于获取当前消费的topic名称。

        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()){
            Object msg = message.get();

            log.info(KafkaProducer.TOPIC_TEST2 + " 消费了: Topic: " + topic + ", Message: " + msg);

            System.out.println(ack.toString());

            ack.acknowledge();
        }
    }


}

