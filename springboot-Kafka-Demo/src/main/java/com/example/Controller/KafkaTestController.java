package com.example.Controller;

import com.example.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KafkaTestController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping("/send")
    public String Send(String msg){
        kafkaProducer.send(msg);
        kafkaProducer.send(msg,"------发送消息--------------");
        return "ok";
    }
}
