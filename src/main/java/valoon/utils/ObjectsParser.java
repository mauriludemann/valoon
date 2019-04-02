package valoon.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import valoon.core.Client;
import valoon.core.Dealer;
import valoon.core.Product;
import valoon.exceptions.ValoonParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ObjectsParser {

    private ObjectMapper mapper;

    public ObjectsParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<Product> getProducts() {
        try {
            return mapper.readValue(new File("./src/main/resources/products.json"), new TypeReference<ArrayList<Product>>() {
            });
        } catch (IOException e) {
            System.out.println("There was an error parsing products");
            e.printStackTrace();
            throw new ValoonParserException("There was an error parsing products");
        }
    }

    public List<Dealer> getDealers() {
        try {
            return mapper.readValue(new File("./src/main/resources/dealers.json"), new TypeReference<ArrayList<Dealer>>() {
            });
        } catch (IOException e) {
            System.out.println("There was an error parsing dealers");
            e.printStackTrace();
            throw new ValoonParserException("There was an error parsing dealers");
        }
    }

    public List<Client> getClients() {
        try {
            return mapper.readValue(new File("./src/main/resources/clients.json"), new TypeReference<ArrayList<Client>>() {
            });
        } catch (IOException e) {
            System.out.println("There was an error parsing clients");
            e.printStackTrace();
            throw new ValoonParserException("There was an error parsing clients");
        }
    }
}
