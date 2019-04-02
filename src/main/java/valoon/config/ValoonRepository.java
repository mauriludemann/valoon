package valoon.config;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ValoonRepository extends AbstractDao {

    @PersistenceContext(name = "ValoonPU")
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
