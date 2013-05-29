package org.cedj.app.web.rest.conference.test;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

import org.cedj.app.domain.Repository;
import org.cedj.app.domain.conference.model.Conference;

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
}
