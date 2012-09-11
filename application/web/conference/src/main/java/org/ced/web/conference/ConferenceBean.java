package org.ced.web.conference;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ced.domain.conference.model.Conference;

@RequestScoped @Named("conference")
public class ConferenceBean {

	@Inject @Current
	private Conference conference;
	
	public Conference getCurrent() {
		return conference;
	}
}
