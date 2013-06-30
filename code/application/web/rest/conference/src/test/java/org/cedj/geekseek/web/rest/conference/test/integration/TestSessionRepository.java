package org.cedj.geekseek.web.rest.conference.test.integration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Session;

@ApplicationScoped
public class TestSessionRepository implements Repository<Session> {

    @Inject
    private TestConferenceRepository conferenceRepository;

    @Override
    public Class<Session> getType() {
        return Session.class;
    }

    @Override
    public Session get(String id) {
        return conferenceRepository.getSessionById(id);
    }

    @Override
    public void remove(Session entity) {
        Conference conf = conferenceRepository.getConferenceBySessionId(entity.getId());
        conf.removeSession(entity);
    }

    @Override
    public Session store(Session entity) {
        return entity;
    }
}
