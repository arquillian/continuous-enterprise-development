package org.ced.domain.user;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.ced.domain.Identifiable;

@Entity
public class User implements Identifiable {
    
    @Id // twitter handle
    private String id;
    
    private String bio;
    
    
    public User(String id) {
	this.id = id;
    }
    
    public String getId() {
	return id;
    }
    
    public String getBio() {
	return bio;
    }
    
    public void setBio(String bio) {
	this.bio = bio;
    }
}
