package org.cedj.geekseek.domain.conference;

import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.domain.persistence.PersistenceRepository;

public class SessionRepository extends PersistenceRepository<Session> {

    public SessionRepository() {
        super(Session.class);
    }
}
