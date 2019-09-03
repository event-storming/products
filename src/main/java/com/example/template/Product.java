package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;

import javax.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    String name;
    int price;
    int stock;
    String imageUrl;

    @PostPersist @PostUpdate
    private void publishStart() {
        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        ProductChanged productChanged = new ProductChanged();
        productChanged.setProductId(this.id);
        productChanged.setProductName(this.name);
        productChanged.setProductPrice(this.price);
        productChanged.setProductStock(this.stock);
        productChanged.setImageUrl(this.imageUrl);
        try {
            json = objectMapper.writeValueAsString(productChanged);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        if( json != null ){
            Environment env = Application.applicationContext.getEnvironment();
            String topicName = env.getProperty("eventTopic");
            ProducerRecord producerRecord = new ProducerRecord<>(topicName, json);
            kafkaTemplate.send(producerRecord);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
