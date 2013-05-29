package org.cedj.app.web.rest.conference.test;

import org.cedj.app.domain.conference.ConferenceDeployments;
import org.cedj.app.web.rest.conference.ConferenceResource;
import org.cedj.app.web.rest.conference.model.ConferenceRepresentation;
import org.cedj.app.web.rest.core.Resource;
import org.cedj.app.web.rest.core.root.RootResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class ConferenceRestDeployments {

    private ConferenceRestDeployments() {
    }

    public static WebArchive conference() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(ConferenceDeployments.domain())
            .addPackage(ConferenceResource.class.getPackage())
            .addPackage(ConferenceRepresentation.class.getPackage())
            .addPackages(true, RootResource.class.getPackage(), Resource.class.getPackage())
            .addClasses(TestApplication.class, TestRepository.class);
    }
}
