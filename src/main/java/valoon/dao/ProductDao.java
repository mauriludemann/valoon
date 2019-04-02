package valoon.dao;

import org.springframework.stereotype.Component;
import valoon.config.ValoonRepository;
import valoon.core.Product;
import valoon.dto.ProductDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDao extends ValoonRepository {

    public void createProduct(Product product) {
        create(product);
    }

    public Product getProduct(Long productId) {
        return find(Product.class, productId);
    }

    public List<Product> getProducts(List<ProductDto> productDtoList) {
        // Agregar la validación de que ningún producto sea nulo
        return productDtoList.stream().map(productDto -> {
            return find(Product.class, productDto.getProductId());
        }).collect(Collectors.toList());
    }
}
