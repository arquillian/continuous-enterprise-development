package org.ced.domain.venue;

import javax.ejb.Stateless;

import org.ced.domain.Repository;

@Stateless
public class VenueRepository extends Repository<Venue> {

	public VenueRepository() {
		super(Venue.class);
	}
}
