package valoon.dao;

import org.springframework.stereotype.Component;
import valoon.config.ValoonRepository;
import valoon.core.Dealer;
import valoon.core.Rate;
import valoon.core.ValoonUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RateDao extends ValoonRepository {

    private static final String RATE_JPQL = "SELECT r FROM Rate r WHERE r.dealer = :dealer";

    public void createRate(Rate rate) {
        create(rate);
    }

    public List<Rate> getAssociatedRates(Dealer dealer) {
        Map<String, ValoonUser> params = new HashMap<String, ValoonUser>(){{
            put("dealer", dealer);
        }};
        return find(Rate.class, RATE_JPQL, params);
    }
}
