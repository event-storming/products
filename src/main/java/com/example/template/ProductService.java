package com.example.template;

import com.example.template.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

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
            if( orderPlaced.isMe()){

                Optional<Product> productOptional = productRepository.findById(orderPlaced.getProductId());
                Product product = productOptional.get();
                product.setStock(product.getStock() - orderPlaced.getQuantity());

                productRepository.save(product);

            }

            /**
             * 주문 취소시, 수량을 늘인다
             */
            if( orderPlaced.getEventType().equals(OrderCancelled.class.getSimpleName())){
                Optional<Product> productOptional = productRepository.findById(orderPlaced.getProductId());
                Product product = productOptional.get();
                product.setStock(product.getStock() + orderPlaced.getQuantity());

                productRepository.save(product);
            }

        }catch (Exception e){

        }
    }

    @Autowired
    ProductRepository productRepository;

    /**
     * 상품 조회
     */
    public Product getProductById(Long id){

        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.get();

        return product;
    }

    public Product save(String data){
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        try {
            product = mapper.readValue(data, Product.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ProductOption> productOptions = product.getProductOptions();
        for(ProductOption p : productOptions){
            p.setProduct(product);
        }

        return productRepository.save(product);
    }
}
