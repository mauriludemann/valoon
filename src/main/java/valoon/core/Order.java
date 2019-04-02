package valoon.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import valoon.statics.OrderStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "valoon_order")
public class Order {

    @JsonProperty(value = "valoon_order_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "valoon_order_id")
    private long orderId;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne()
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;

    @OneToMany(mappedBy = "valoonOrder")
    private Set<ProductOrder> productOrders = new HashSet<>();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "total_amount")
    private Double totalAmount;

    public Order() { }

    public Order(Client client, Set<ProductOrder> productOrders, Double deliveryPrice) {
        this.client = client;
        this.status = OrderStatus.PENDING;
        this.productOrders = productOrders;
        this.totalAmount = productOrders.stream().mapToDouble(ProductOrder::getTotalAmount).sum() + deliveryPrice;
    }

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
