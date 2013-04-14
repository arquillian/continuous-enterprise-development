package org.ced.domain.conference;

import org.ced.domain.CoreDeployments;
import org.ced.domain.conference.model.Conference;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class ConferenceDeployments {

    public static JavaArchive conference() {
    	return CoreDeployments.core()
    		.addPackage(Conference.class.getPackage())
    	    .addPackage(ConferenceRepository.class.getPackage());
    }
}
