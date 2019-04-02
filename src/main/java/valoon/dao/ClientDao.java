package valoon.dao;

import org.springframework.stereotype.Component;
import valoon.config.ValoonRepository;
import valoon.core.Client;

@Component
public class ClientDao extends ValoonRepository {

    public void createClient(Client client) {
        create(client);
    }

    public Client getClient(Long clientId) {
        return find(Client.class, clientId);
    }
}
