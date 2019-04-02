package valoon.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "product_order")
public class ProductOrder {

    @JsonProperty(value = "product_order_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_order_id")
    private long orderId;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "valoon_order_id")
    private Order valoonOrder;

    public ProductOrder() { }

    public ProductOrder(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Order getValoonOrder() {
        return valoonOrder;
    }

    public void setValoonOrder(Order order) {
        this.valoonOrder = order;
    }

    public Double getTotalAmount() {
        return product.getPrice() * quantity;
    }

}
