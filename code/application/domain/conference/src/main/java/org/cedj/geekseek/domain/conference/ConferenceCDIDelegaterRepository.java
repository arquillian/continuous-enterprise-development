package org.cedj.geekseek.domain.conference;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Conference;

@ApplicationScoped
public class ConferenceCDIDelegaterRepository implements Repository<Conference> {

    @EJB
    private ConferenceRepository repo;

    public Class<Conference> getType() {
        return repo.getType();
    }

    public Conference store(Conference entity) {
        return repo.store(entity);
    }

    public Conference get(String id) {
        return repo.get(id);
    }

    public void remove(Conference entity) {
        repo.remove(entity);
    }
}
