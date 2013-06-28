package org.cedj.geekseek.web.rest.attachment.test;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.attachment.model.Attachment;

@RequestScoped
public class TestRepository implements Repository<Attachment> {

    private static Set<Attachment> conferences = new HashSet<Attachment>();

    @Override
    public Attachment store(Attachment entity) {
        conferences.add(entity);
        return entity;
    }

    @Override
    public Attachment get(String id) {
        for(Attachment conference: conferences) {
            if(conference.getId().equals(id)) {
                return conference;
            }
        }
        return null;
    }

    @Override
    public void remove(Attachment entity) {
        conferences.remove(entity);
    }

    public static void addConference(Attachment conference) {
        conferences = new HashSet<Attachment>();
        conferences.add(conference);
    }

    @Override
    public Class<Attachment> getType() {
        return Attachment.class;
    }
}
