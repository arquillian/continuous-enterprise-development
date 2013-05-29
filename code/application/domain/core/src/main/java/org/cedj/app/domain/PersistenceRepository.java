package org.cedj.app.domain;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.cedj.app.domain.model.Identifiable;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public abstract class PersistenceRepository<T extends Identifiable> implements Repository<T> {

    @PersistenceContext
    private EntityManager manager;

    private Class<T> type;

    public PersistenceRepository(Class<T> type) {
        this.type = type;
    }

    public T store(T entity) {
        T merged = merge(entity);
        manager.persist(merged);
        return merged;
    }

    public T get(String id) {
        return manager.find(type, id);
    }

    public void remove(T entity) {
        manager.remove(merge(entity));
    }

    private T merge(T entity) {
        return manager.merge(entity);
    }

    protected EntityManager getManager() {
        return manager;
    }
}
