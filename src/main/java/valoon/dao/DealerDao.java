package valoon.dao;

import org.springframework.stereotype.Component;
import valoon.config.ValoonRepository;
import valoon.core.Dealer;

@Component
public class DealerDao extends ValoonRepository {

    public void createDealer(Dealer dealer) {
        create(dealer);
    }

    public Dealer getDealer(Long dealerId) {
        return find(Dealer.class, dealerId);
    }
}
