package org.cedj.geekseek.domain.conference;

import javax.ejb.Stateless;

import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.domain.persistence.PersistenceRepository;

@Stateless
public class SessionRepository extends PersistenceRepository<Session> {

    public SessionRepository() {
        super(Session.class);
    }
}
