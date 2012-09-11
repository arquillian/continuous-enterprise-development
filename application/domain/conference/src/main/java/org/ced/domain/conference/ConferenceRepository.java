package org.ced.domain.conference;

import javax.ejb.Stateless;

import org.ced.domain.Repository;
import org.ced.domain.conference.model.Conference;

@Stateless
public class ConferenceRepository extends Repository<Conference> {

    public ConferenceRepository() {
	super(Conference.class);
    }
}
