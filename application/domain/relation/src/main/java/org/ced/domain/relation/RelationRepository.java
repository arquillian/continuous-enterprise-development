package org.ced.domain.relation;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.ced.domain.Identifiable;

@Stateless
public class RelationRepository {

    @PersistenceContext
    private EntityManager manager;
    
    public Relation add(Identifiable source, Relation.Type type, Identifiable target) {
	Relation rel = new Relation(source.getId(), target.getId(), type);
	manager.persist(rel);
	return rel;
    }
    
    // TODO: implement as criteria query ?
    public <T extends Identifiable> List<T> findTargets(Identifiable source, Relation.Type type, Class<T> targetType) {

	StringBuilder q = new StringBuilder();
	q.append("select t from " + targetType.getSimpleName() + " t , Relation r ");
	q.append("where t.id = r.key.targetId ");
	q.append("and r.key.sourceId = :source ");
	q.append("and r.key.type = :type");
	
	TypedQuery<T> query = manager.createQuery(q.toString(), targetType);
	query.setParameter("source", source.getId());
	query.setParameter("type", type);
	
	return query.getResultList();
    }
}
