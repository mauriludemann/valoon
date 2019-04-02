package valoon.statics;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    PENDING,ACCEPTED,CONFIRMED,DELIVERED,CANCELLED,RATED;

    private static final List<OrderStatus> CANCELLABLE_STATUS = Arrays.asList(PENDING, ACCEPTED);

    public Boolean isCancellable() {
        return CANCELLABLE_STATUS.contains(this);
    }
}
