package com.example.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Producer {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.16.140:9092,192.168.16.140:9093,192.168.16.140:9094");
        /**
        发出消息持久化机制参数
            (1) acks=0
         表示producer不需要等待任何broker确认收到消息的回复，就可以继续发送下一条消息。性能最高，但是最容易丢消息。
            (2) acks=1
         至少要等待leader已经成功将数据写入本地log，但是不需要等待所有follower是否成功写入。
         就可以继续发送下一条消息。这种情况下，如果follower没有成功备份数据，而此时leader又挂掉，则消息会丢失。
            (3) acks=-1或All
         这意味着leader需要等待所有备份(min.insync.replicas配置的备份个数)都成功写入日志，这种策略会保证只要有一个备份存活就不会丢失数据。
         这是最强的数据保证。一般除非是金融级别，或跟钱打交道的场景才会使用这种配置。
         */
        props.put(ProducerConfig.ACKS_CONFIG,"1");
        //发送失败会重试，默认重试间隔100ms，重发的次数
        props.put(ProducerConfig.RETRIES_CONFIG,3);
        //重试间隔设置
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,300);
        //设置发送消息的本地缓冲区，如果设置了该缓冲区，消息会先发送到本地缓冲区，可以提高消息发送性能，默认值是33554432，即32MB
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        //kafka本地线程会从缓冲区取数据，批量发送到broker
        //设置批量发送消息的大小，默认值是16384，即16kb，就是说一个batch满了16kb就发送出
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        //默认值是0，意思就是消息必须立即被发送，但这样会影响性能
        //一般设置100毫秒左右，就是说这个消息发送完后会进入本地的一个batch，如果100毫秒内，这个batch满了16kb就会随batch一起被发送出去
        //如果100毫秒内，batch没满，那么也必须把消息发送出去，不能让消息的发送延迟时间太长
        props.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        //把发送的key从字符串序列化为字节数组
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    }
}