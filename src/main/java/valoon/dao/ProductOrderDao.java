package valoon.dao;

import org.springframework.stereotype.Component;
import valoon.config.ValoonRepository;
import valoon.core.Product;
import valoon.core.ProductOrder;
import valoon.dto.ProductDto;

import java.util.List;
import java.util.Set;

@Component
public class ProductOrderDao extends ValoonRepository {

    public ProductOrder createProduct(ProductOrder productOrder) {
        return create(productOrder);
    }
}
