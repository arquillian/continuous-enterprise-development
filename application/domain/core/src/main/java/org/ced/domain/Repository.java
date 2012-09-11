package org.ced.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Repository<T extends Identifiable> {

    @PersistenceContext
    private EntityManager manager;
    
    private Class<T> type;
    
    public Repository(Class<T> type) {
	this.type = type;
    }
    
    public T add(T entity) {
	manager.persist(entity);
	return entity;
    }
    
    public T get(String id) {
	return manager.find(type, id);
    }
    
    protected EntityManager getManager() {
	return manager;
    }
}
