package com.example.template;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {


    private static final String RESPONSE_STRING_FORMAT = "product change from '%s': %d";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ProductService productService;

    private int count = 0;

    private static final String HOSTNAME = parseContainerIdFromHostname(
            System.getenv().getOrDefault("HOSTNAME", "deliveries"));

    static String parseContainerIdFromHostname(String hostname) {
        return hostname.replaceAll("deliveries-v\\d+-", "");
    }


    @GetMapping("/product/{productId}")
    Product productStockCheck(@PathVariable(value = "productId") Long productId) {

        System.out.println("productStockCheck call");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return  this.productService.getProductById(productId);
    }

    @PostMapping("/product")
    Product productInsert(@RequestBody String data) {
        System.out.println(data);
        return this.productService.save(data);
    }


    @PatchMapping("/product/{productId}")
    @HystrixCommand(fallbackMethod = "certifyFallBack")
    ResponseEntity<String> fakeProductPatch(@PathVariable(value = "productId") Long productId, @RequestBody String data) {

        count++;
        logger.info(String.format("product start from %s: %d", HOSTNAME, count));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(String.format(RESPONSE_STRING_FORMAT, HOSTNAME, count));
    }

    ResponseEntity<String> certifyFallBack(Long productId, String data){

        System.out.println("certifyFallBack");
        System.out.println(data);

        return ResponseEntity.ok("시스템이 혼잡합니다!! 잠시후 다시 호출해주세요");
    }
}
