package org.cedj.geekseek.web.rest.conference.test;

import org.cedj.geekseek.domain.conference.test.ConferenceDeployments;
import org.cedj.geekseek.web.rest.conference.ConferenceResource;
import org.cedj.geekseek.web.rest.conference.model.ConferenceRepresentation;
import org.cedj.geekseek.web.rest.core.test.RestCoreDeployments;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class ConferenceRestDeployments {

    private ConferenceRestDeployments() {
    }

    public static WebArchive conference() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(ConferenceDeployments.domain(), RestCoreDeployments.root())
            .addPackage(ConferenceResource.class.getPackage())
            .addPackage(ConferenceRepresentation.class.getPackage())
            .addClasses(TestApplication.class, TestRepository.class);
    }
}
