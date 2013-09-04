package org.cedj.geekseek.web.rest.attachment.test.integration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.attachment.model.Attachment;

@RequestScoped
public class TestRepository implements Repository<Attachment> {

    private static Map<String, Attachment> conferences = new HashMap<String, Attachment>();

    @Override
    public Attachment store(Attachment entity) {
        conferences.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Attachment get(String id) {
        return conferences.get(id);
    }

    @Override
    public void remove(Attachment entity) {
        conferences.remove(entity.getId());
    }

    public static void addConference(Attachment conference) {
        conferences = new HashMap<String, Attachment>();
        conferences.put(conference.getId(), conference);
    }

    @Override
    public Class<Attachment> getType() {
        return Attachment.class;
    }
}
