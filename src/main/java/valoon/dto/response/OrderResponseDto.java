package valoon.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import valoon.core.Client;
import valoon.core.Dealer;
import valoon.core.ProductOrder;
import valoon.statics.OrderStatus;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponseDto {

    @JsonProperty(value = "valoon_order_id")
    private long orderId;

    private Client client;

    private Dealer dealer;

    @JsonProperty(value = "product_orders")
    private Set<ProductOrder> productOrders;

    private OrderStatus status;

    @JsonProperty(value = "total_amount")
    private Double totalAmount;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Set<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(Set<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
