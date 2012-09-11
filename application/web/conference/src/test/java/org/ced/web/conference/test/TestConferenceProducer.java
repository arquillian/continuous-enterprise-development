package org.ced.web.conference.test;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.ced.domain.conference.model.Conference;
import org.ced.web.conference.Current;

@RequestScoped
public class TestConferenceProducer {
	
	private Conference conference;
	
	public void setConference(Conference conference) {
		this.conference = conference;
	}
	
	@Produces @Current
	public Conference getConference() {
		return conference;
	}
}
