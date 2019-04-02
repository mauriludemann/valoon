package valoon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDto {

    @JsonProperty(value = "product_id")
    private Long productId;

    @JsonProperty(value = "quantity")
    private Long quantity;

    public ProductDto() { }

    public ProductDto(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
