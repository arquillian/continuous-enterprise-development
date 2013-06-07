package org.cedj.geekseek.domain.relation;

import java.util.List;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.relation.model.Relation;

public interface RelationRepository {

    Relation add(Identifiable source, String type, Identifiable target);

    void remove(Identifiable source, String type, Identifiable target);

    <T extends Identifiable> List<T> findTargets(Identifiable source, String type, Class<T> targetType);
}