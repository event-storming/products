package com.example.template;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    @KafkaListener(topics = "${eventTopic}")
    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("##### listener : " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OrderPlaced orderPlaced = null;
        try {
            orderPlaced = objectMapper.readValue(message, OrderPlaced.class);

            /**
             * 주문이 발생시, 수량을 줄인다.
             */
            if( orderPlaced.getEventType().equals(OrderPlaced.class.getSimpleName())){

                Optional<Product> productOptional = productRepository.findById(orderPlaced.getProductId());
                Product product = productOptional.get();
                product.setStock(product.getStock() - orderPlaced.getQuantity());

                productRepository.save(product);

            }

        }catch (Exception e){

        }
    }
}
