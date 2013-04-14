package org.ced.domain.venue;

import javax.ejb.Stateless;

import org.ced.domain.Repository;
import org.ced.domain.venue.model.Venue;

@Stateless
public class VenueRepository extends Repository<Venue> {

	public VenueRepository() {
		super(Venue.class);
	}
}
