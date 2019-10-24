package com.example.template;

import com.example.template.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onOrderPlaced(@Payload String message) {
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

//    @KafkaListener(topics = "${eventTopic}")
//    public void onOrderPlaced(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
//        System.out.println("##### listener : " + message);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        OrderPlaced orderPlaced = null;
//        try {
//            orderPlaced = objectMapper.readValue(message, OrderPlaced.class);
//
//            /**
//             * 주문이 발생시, 수량을 줄인다.
//             */
//            if( orderPlaced.getEventType().equals(OrderPlaced.class.getSimpleName())){
//
//                Optional<Product> productOptional = productRepository.findById(orderPlaced.getProductId());
//                Product product = productOptional.get();
//                product.setStock(product.getStock() - orderPlaced.getQuantity());
//
//                productRepository.save(product);
//
//            }
//
//        }catch (Exception e){
//
//        }
//    }

    /**
     * 상품 조회
     */
    public Product getProductById(Long id){

        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.get();

        return product;
    }
}
