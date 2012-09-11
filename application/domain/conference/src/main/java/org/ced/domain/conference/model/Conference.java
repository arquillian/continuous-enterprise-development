package org.ced.domain.conference.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.ced.domain.Identifiable;

@Entity
public class Conference implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    private String name;

    private String tagLine;
    
    @Embedded @Valid @NotNull
    private Duration duration;
    
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true) @Valid
    private Set<Session> sessions;

    public Conference() {
	this.id = UUID.randomUUID().toString();
    }

    public String getId() {
	return id;
    }

    public String getName() {
	return name;
    }
    
    public Conference setName(String name) {
	this.name = name;
	return this;
    }
    
    public String getTagLine() {
	return tagLine;
    }
    
    public Conference setTagLine(String tagLine) {
	this.tagLine = tagLine;
	return this;
    }

    public Conference setDuration(Duration duration) {
	this.duration = duration;
	return this;
    }
    
    public Duration getDuration() {
	return duration;
    }

    public Set<Session> getSessions() {
	return Collections.unmodifiableSet(sessions);
    }

    public Conference addSession(Session session) {
	if(sessions == null) {
	    this.sessions = new HashSet<Session>();
	}
	if(!sessions.contains(session)) {
	    sessions.add(session);
	}
	return this;
    }
}
