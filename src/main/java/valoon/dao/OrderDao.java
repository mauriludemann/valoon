package valoon.dao;

import org.springframework.stereotype.Component;
import valoon.config.ValoonRepository;
import valoon.core.Order;
import valoon.core.ValoonUser;
import valoon.statics.ValoonUserType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDao extends ValoonRepository {

    private static final String CLIENT_JPQL = "SELECT o FROM Order o WHERE o.client = :client";
    private static final String DEALER_JPQL = "SELECT o FROM Order o WHERE o.dealer = :dealer";

    public Order createOrder(Order order) {
        return create(order);
    }

    public Order getOrder(Long orderId) {
        return find(Order.class, orderId);
    }

    public List<Order> getAssociatedOrdersToValoonUser(ValoonUserType userType, ValoonUser valoonUser) {
        Map<String, ValoonUser> params = new HashMap<String, ValoonUser>(){{
            put(userType.toString(), valoonUser);
        }};
        return userType == ValoonUserType.CLIENT ? find(Order.class, CLIENT_JPQL, params) : find(Order.class, DEALER_JPQL, params);
    }
}
