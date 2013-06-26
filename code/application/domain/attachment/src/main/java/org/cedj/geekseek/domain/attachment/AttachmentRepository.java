package org.cedj.geekseek.domain.attachment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.infinispan.Cache;

@ApplicationScoped
public class AttachmentRepository implements Repository<Attachment> {

    @Inject
    private Cache<Object, Object> cache;

    @Override
    public Class<Attachment> getType() {
        return Attachment.class;
    }

    @Override
    public Attachment store(Attachment entity) {
        cache.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Attachment get(String id) {
        return (Attachment)cache.get(id);
    }

    @Override
    public void remove(Attachment entity) {
        cache.remove(entity.getId());
    }
}
