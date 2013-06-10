package org.cedj.geekseek.domain.persistence.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.model.Timestampable;

@MappedSuperclass
public abstract class BaseEntity implements Identifiable, Timestampable, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    // JPA default constructor
    protected BaseEntity() {
    }

    public BaseEntity(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public Date getLastUpdated() {
        return updated == null ? null:(Date)updated.clone();
    }

    @Override
    public Date getCreated() {
        return created == null ? null:(Date)created.clone();
    }

    @Override
    public Date getLastModified() {
        return getLastUpdated() == null ? getCreated():getLastUpdated();
    }

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}
