package com.example.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    protected static ApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(Application.class, args);

        ProductRepository productRepository = applicationContext.getBean(ProductRepository.class);
        // 초기 상품 셋팅
        String[] products = {"TV", "RADIO", "NOTEBOOK", "TABLE", "CLOCK"};
        int i = 1;
        for(String p : products){
            Product product = new Product();
            product.setName(p);
            product.setPrice(i*10000);
            product.setStock(i*10);
            product.setImageUrl("https://github.githubassets.com/images/modules/profile/profile-joined-github.png");
            i++;
            productRepository.save(product);
        }
    }



}

