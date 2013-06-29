package org.cedj.geekseek.web.rest.conference.test.integration;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Conference;

@RequestScoped
public class TestRepository implements Repository<Conference> {

    private static Set<Conference> conferences = new HashSet<Conference>();

    @Override
    public Conference store(Conference entity) {
        conferences.add(entity);
        return entity;
    }

    @Override
    public Conference get(String id) {
        for(Conference conference: conferences) {
            if(conference.getId().equals(id)) {
                return conference;
            }
        }
        return null;
    }

    @Override
    public void remove(Conference entity) {
        conferences.remove(entity);
    }

    public static void addConference(Conference conference) {
        conferences = new HashSet<Conference>();
        conferences.add(conference);
    }

    @Override
    public Class<Conference> getType() {
        return Conference.class;
    }
}
