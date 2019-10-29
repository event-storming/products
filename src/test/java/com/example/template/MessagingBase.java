package com.example.template;

import com.example.template.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)    // junit4 와 springboot 를 연결해준다.
// 통합테스트
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public abstract class MessagingBase {

    //remove::start[]
    @Autowired
    MessageVerifier messaging;
    //remove::end[]

    @Autowired
    KafkaProcessor kafkaProcessor;

    @Before
    public void setup() {
        // let's clear any remaining messages
        // output == destination or channel name
        //remove::start[]
        this.messaging.receive(kafkaProcessor.outboundTopic().toString(), 100, TimeUnit.MILLISECONDS);
        //remove::end[]
    }

    public void productChanged() {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        ProductChanged productChanged = new ProductChanged();
        productChanged.setProductId(1L);
//        productChanged.setProductTitle("TEST");
        productChanged.setProductName("TEST");
        productChanged.setProductPrice(10000);
        productChanged.setProductStock(10);
        productChanged.setImageUrl("/test.jpg");
        try {
            json = objectMapper.writeValueAsString(productChanged);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        System.out.println("test output Topic = " + kafkaProcessor.outboundTopic().toString());

        this.messaging.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build(), kafkaProcessor.outboundTopic().toString());
    }
}
