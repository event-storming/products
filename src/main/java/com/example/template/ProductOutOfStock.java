package com.example.template;

public class ProductOutOfStock extends AbstractEvent {

    private String stateMessage = "재고량 바닥";
    private Long productId;
    private Long orderId;

    public ProductOutOfStock(){
        super();
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
