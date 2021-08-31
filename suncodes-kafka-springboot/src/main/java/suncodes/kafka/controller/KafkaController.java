package suncodes.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import suncodes.kafka.producer.MyKafkaProducer;

@RestController
public class KafkaController {

    @Autowired
    private MyKafkaProducer myKafkaProducer;

    @GetMapping("/f")
    public String f() {
        myKafkaProducer.kafkaSend();
        return "success";
    }
}
