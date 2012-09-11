package org.ced.domain.venue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import org.ced.domain.Identifiable;

@Entity
public class Venue implements Identifiable {

	@Id
	private String id;

	@OneToMany(fetch = FetchType.EAGER) @Valid
	private Set<Room> rooms;

	public Venue() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public Set<Room> getRooms() {
		return Collections.unmodifiableSet(rooms);
	}

	public Venue addRoom(Room room) {
		if (rooms == null) {
			this.rooms = new HashSet<Room>();
		}
		if (!rooms.contains(room)) {
			rooms.add(room);
		}
		return this;
	}
}
