package org.cedj.geekseek.web.rest.conference.test.integration;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;

@ApplicationScoped
public abstract class TestRepository<T extends Identifiable> implements Repository<T> {

    private Set<T> conferences = new HashSet<T>();

    private Class<T> type;

    // for CDI proxyabillity
    protected TestRepository() {}

    public TestRepository(Class<T> type) {
        this.type = type;
    }

    protected Set<T> getStored() {
        return conferences;
    }

    @Override
    public T store(T entity) {
        conferences.add(entity);
        return entity;
    }

    @Override
    public T get(String id) {
        for(T conference: conferences) {
            if(conference.getId().equals(id)) {
                return conference;
            }
        }
        return null;
    }

    @Override
    public void remove(T entity) {
        conferences.remove(entity);
    }

    @Override
    public Class<T> getType() {
        return type;
    }
}
