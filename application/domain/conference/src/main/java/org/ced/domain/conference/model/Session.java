package org.ced.domain.conference.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    
    @Embedded @NotNull @Valid
    private Duration duration;
    
    @NotNull
    private String title;
    private String outline;
    
    public Session() {
	this.id = UUID.randomUUID().toString();
    }
    
    public String getId() {
	return id;
    }
    
    public Duration getDuration() {
	return duration;
    }
    
    public void setDuration(Duration duration) {
	this.duration = duration;
    }

    public String getTitle() {
	return title;
    }
    
    public void setTitle(String title) {
	this.title = title;
    }
    
    public String getOutline() {
	return outline;
    }
    
    public void setOutline(String outline) {
	this.outline = outline;
    }
}
