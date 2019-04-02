package valoon.config;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDao {

    protected abstract EntityManager getEntityManager();

    @Transactional
    public <E> E create(E entity) {
        getEntityManager().persist(entity);
        return entity;
    }

    @Transactional
    public <E> E find(Class<E> entityClass, Object id) {
        return getEntityManager().find(entityClass, id);
    }

    @Transactional
    public <E> List<E> find(Class<E> entityClass, String jpql, Map<String, ?> params) {
        TypedQuery<E> query = createQuery(jpql, entityClass);
        Optional.ofNullable(params).ifPresent((map) -> map.forEach(query::setParameter));
        return query.getResultList();
    }

    private <E> TypedQuery<E> createQuery(String jpql, Class<E> entityClass) {
        EntityManager em = getEntityManager();
        return em.createQuery(jpql, entityClass);
    }
}
