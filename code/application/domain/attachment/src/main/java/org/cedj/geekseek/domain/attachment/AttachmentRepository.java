package org.cedj.geekseek.domain.attachment;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.infinispan.AdvancedCache;
import org.infinispan.context.Flag;

@Stateless
@LocalBean
@Typed(AttachmentRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AttachmentRepository implements Repository<Attachment> {

    @Inject
    private AdvancedCache<String, Attachment> cache;

    @Override
    public Class<Attachment> getType() {
        return Attachment.class;
    }

    @Override
    public Attachment store(Attachment entity) {
        try {
            cache.withFlags(Flag.SKIP_REMOTE_LOOKUP, Flag.SKIP_CACHE_LOAD, Flag.IGNORE_RETURN_VALUES)
                .put(entity.getId(), entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Could not store Attachment with id " + entity.getId(), e);
        }
    }

    @Override
    public Attachment get(String id) {
        try {
            return cache.get(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not retreive Attachment with id " + id, e);
        }
    }

    @Override
    public void remove(Attachment entity) {
        cache.withFlags(Flag.SKIP_REMOTE_LOOKUP, Flag.SKIP_CACHE_LOAD, Flag.IGNORE_RETURN_VALUES)
            .remove(entity.getId());
    }
}
