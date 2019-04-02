package valoon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderDto {

    @JsonProperty(value = "product_list")
    private List<ProductDto> productDtoList;

    @JsonProperty
    private String client_id;

    public CreateOrderDto() {
        this.productDtoList = new ArrayList<>();
    }

    public void addProductDto(ProductDto productDto) {
        this.productDtoList.add(productDto);
    }

    public List<ProductDto> getProductDtoList() {
        return productDtoList;
    }

    public void setProductDtoList(List<ProductDto> productDtoList) {
        this.productDtoList = productDtoList;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }
}
