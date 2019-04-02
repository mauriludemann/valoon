package valoon.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import valoon.core.Product;


import java.util.List;

public class ObjectsParserTest {

    private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(ObjectsParserTest.class.getName());

    @Autowired
    private ObjectsParser objectsParser;

    @Test
    public void testGetProducts() {
        String lineSeparator = System.getProperty("line.separator");

        LOGGER.info(String.format("RETRIEVING PRODUCTS FROM JSON FILE...%s", lineSeparator));

        List<Product> products = objectsParser.getProducts();
        Assert.assertFalse(products.isEmpty());
        Assert.assertEquals(products.size(), 10);

        System.out.println(String.format(">>>>> AVAILABLE PRODUCTS <<<<< %s" , lineSeparator));
        for (Product product: products)
            System.out.println(String.format("NAME: %s - PRICE: $%s", product.getName(), product.getPrice()));

    }
}
