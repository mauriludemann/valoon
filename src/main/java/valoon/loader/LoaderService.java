package valoon.loader;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import valoon.core.Client;
import valoon.core.Dealer;
import valoon.core.Product;
import valoon.dao.ClientDao;
import valoon.dao.DealerDao;
import valoon.dao.ProductDao;
import valoon.utils.ObjectsParser;

import java.util.List;

@Service
public class LoaderService {

    private ClientDao clientDao;
    private DealerDao dealerDao;
    private ProductDao productDao;
    private ObjectsParser objectsParser;

    public LoaderService(ClientDao clientDao, DealerDao dealerDao, ProductDao productDao, ObjectsParser objectsParser) {
        this.clientDao = clientDao;
        this.dealerDao = dealerDao;
        this.productDao = productDao;
        this.objectsParser = objectsParser;
    }

    @Transactional
    public void createInformation() {
        List<Dealer> dealers = objectsParser.getDealers();
        List<Client> clients = objectsParser.getClients();
        List<Product> products = objectsParser.getProducts();
        dealers.forEach(dealerDao::createDealer);
        clients.forEach(clientDao::createClient);
        products.forEach(productDao::createProduct);
    }

}
