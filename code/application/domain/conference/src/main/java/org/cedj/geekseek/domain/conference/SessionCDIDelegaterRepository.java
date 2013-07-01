package org.cedj.geekseek.domain.conference;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Session;

@ApplicationScoped
public class SessionCDIDelegaterRepository implements Repository<Session> {

    @EJB
    private SessionRepository repo;

    public Class<Session> getType() {
        return repo.getType();
    }

    public Session store(Session entity) {
        return repo.store(entity);
    }

    public Session get(String id) {
        return repo.get(id);
    }

    public void remove(Session entity) {
        repo.remove(entity);
    }
}
