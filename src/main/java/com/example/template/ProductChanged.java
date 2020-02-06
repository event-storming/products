package com.example.template;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductChanged extends AbstractEvent{

    private String stateMessage = "상품 변경이 발생함";

    private Long productId;
    private String productName;
    private int productPrice;
    private int productStock;
    private String imageUrl;

    public ProductChanged(){
        super();
    }

    public ProductChanged(Product product){
        this();
        this.setProductId(product.getId());
        this.setProductName(product.getName());
        this.setProductPrice(product.getPrice());
        this.setProductStock(product.getStock());
        this.setImageUrl(product.getImageUrl());
    }

    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public int getProductStock() {
        return productStock;
    }
    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

}
