package org.cedj.app.domain;

import org.cedj.app.domain.model.Identifiable;

public interface Repository<T extends Identifiable> {

    T store(T entity);

    T get(String id);

    void remove(T entity);
}
