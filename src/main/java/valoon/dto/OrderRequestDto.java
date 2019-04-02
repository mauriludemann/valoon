package valoon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrderRequestDto {

    @JsonProperty(value = "product_list")
    private List<ProductDto> productsList;
    @JsonProperty(value = "client_id")
    private Long ClientId;

    public OrderRequestDto() { }

    public List<ProductDto> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<ProductDto> productsList) {
        this.productsList = productsList;
    }

    public Long getClientId() {
        return ClientId;
    }

    public void setClientId(Long clientId) {
        ClientId = clientId;
    }
}
