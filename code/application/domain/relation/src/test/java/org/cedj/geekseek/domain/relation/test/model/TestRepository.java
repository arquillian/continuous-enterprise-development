package org.cedj.geekseek.domain.relation.test.model;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;

@ApplicationScoped
public abstract class TestRepository<T extends Identifiable> implements Repository<T> {

    private Set<T> data = new HashSet<T>();

    @Override
    public T store(T entity) {
        data.add(entity);
        return entity;
    }

    @Override
    public T get(String id) {
        for(T d : data) {
            if(d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    @Override
    public void remove(T entity) {
        data.remove(entity);
    }

}
