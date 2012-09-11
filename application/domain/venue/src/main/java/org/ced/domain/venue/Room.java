package org.ced.domain.venue;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Room {

	@Id
	private String id;

	@NotNull
	private String name;
	
	public Room() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
