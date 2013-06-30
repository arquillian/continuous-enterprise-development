package org.cedj.geekseek.web.rest.conference.test.integration;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Session;

public class TestConferenceRepository extends TestRepository<Conference> {

    public TestConferenceRepository() {
        super(Conference.class);
    }

    Session getSessionById(String id) {
        for(Conference conf : getStored()) {
            for(Session session : conf.getSessions()) {
                if(session.getId().equals(id)) {
                    return session;
                }
            }
        }
        return null;
    }

    Conference getConferenceBySessionId(String id) {
        for(Conference conf : getStored()) {
            for(Session session : conf.getSessions()) {
                if(session.getId().equals(id)) {
                    return conf;
                }
            }
        }
        return null;
    }
}
