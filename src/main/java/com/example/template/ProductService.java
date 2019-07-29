package com.example.template;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    @KafkaListener(topics = "eventTopic")
    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("##### listener : " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ProductRequired productRequired = null;
        try {
            productRequired = objectMapper.readValue(message, ProductRequired.class);

            /**
             * 상품 추가 요청이 왔을때 해당 상품을 찾아서 재고를 늘린다.
             */
            if( productRequired.getType().equals(ProductRequired.class.getSimpleName())){

                List<Product> productList = productRepository.findByName(productRequired.getProductName());
                Product product = null;
                if( productList != null && productList.size() > 0 ){
                    product = productList.get(0);
                }

                if( product == null ) {
                    product = new Product();
                    product.setName(productRequired.getProductName());
                    product.setPrice(10000);
                    product.setStock(1);
                }

                // product 의 수량을 10개씩 늘린다
                product.setStock(product.getStock() + 10);

                productRepository.save(product);

            }

        }catch (Exception e){

        }
    }
}
