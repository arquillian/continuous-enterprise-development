package org.cedj.geekseek.domain;

import java.util.Collection;

import org.cedj.geekseek.domain.model.Identifiable;

public interface SearchableRepository<T extends Identifiable, X extends SearchableCriteria> {

    Collection<T> search(X criteria);
}
