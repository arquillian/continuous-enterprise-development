package org.cedj.geekseek.web.rest.conference.test.integration;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Session;

@ApplicationScoped
public class TestSessionRepository implements Repository<Session> {

    private Set<Session> sessions = new HashSet<Session>();

    @Inject
    private TestConferenceRepository conferenceRepository;

    @Override
    public Class<Session> getType() {
        return Session.class;
    }

    @Override
    public Session get(String id) {
        Session session = conferenceRepository.getSessionById(id);
        if(session == null) {
            for(Session s: sessions) {
                if(s.getId().equals(id)) {
                    return s;
                }
            }
        }
        return session;
    }

    @Override
    public void remove(Session entity) {
        Conference conf = conferenceRepository.getConferenceBySessionId(entity.getId());
        if(conf != null) {
            conf.removeSession(entity);
        }
        sessions.remove(entity);
    }

    @Override
    public Session store(Session entity) {
        sessions.add(entity);
        return entity;
    }
}
