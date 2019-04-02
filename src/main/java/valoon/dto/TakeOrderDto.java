package valoon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TakeOrderDto {

    @JsonProperty(value = "order_id")
    private Long orderId;
    @JsonProperty(value = "dealer_id")
    private Long dealerId;

    public TakeOrderDto() { }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDealerId() {
        return dealerId;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }
}
