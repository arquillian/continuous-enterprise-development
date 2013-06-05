package org.cedj.geekseek.domain;

import org.cedj.geekseek.domain.model.Identifiable;

public interface Repository<T extends Identifiable> {

    T store(T entity);

    T get(String id);

    void remove(T entity);
}
