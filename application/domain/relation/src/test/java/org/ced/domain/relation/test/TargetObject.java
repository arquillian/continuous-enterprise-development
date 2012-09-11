package org.ced.domain.relation.test;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.ced.domain.Identifiable;

@Entity
public class TargetObject implements Identifiable {

    @Id
    public String id;
    
    @SuppressWarnings("unused")
    private TargetObject() {} // JPA
    
    public TargetObject(String id) {
	this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
