package org.ced.domain.relation;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
public class Relation {
    
    public enum Type {
	SPEAKING, TRACKING, ATTENDING, ORGANIZING
    }

    @EmbeddedId @Valid
    private Key key;

    private Date created;

    @SuppressWarnings("unused")
    private Relation() {} // JPA
    
    public Relation(String sourceId, String targetId, Type type) {
	this.key = new Key(sourceId, targetId, type);
	this.created = new Date();
    }

    public String getSourceId() {
	return key.sourceId;
    }

    public String getTargetId() {
	return key.targetId;
    }

    public Type getType() {
	return key.type;
    }

    public Date getCreated() {
	return (Date)created.clone();
    }

    private static class Key implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull
	private String sourceId;
	
	@NotNull
	private String targetId;
	
	@Enumerated(EnumType.STRING) @NotNull
	private Type type;

	private Key() {} // JPA
	
	private Key(String sourceId, String targetId, Type type) {
	    this.sourceId = sourceId;
	    this.targetId = targetId;
	    this.type = type;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
		    + ((sourceId == null) ? 0 : sourceId.hashCode());
	    result = prime * result
		    + ((targetId == null) ? 0 : targetId.hashCode());
	    result = prime * result + ((type == null) ? 0 : type.hashCode());
	    return result;
	}
	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    Key other = (Key) obj;
	    if (sourceId == null) {
		if (other.sourceId != null)
		    return false;
	    } else if (!sourceId.equals(other.sourceId))
		return false;
	    if (targetId == null) {
		if (other.targetId != null)
		    return false;
	    } else if (!targetId.equals(other.targetId))
		return false;
	    if (type != other.type)
		return false;
	    return true;
	}
    }
}
