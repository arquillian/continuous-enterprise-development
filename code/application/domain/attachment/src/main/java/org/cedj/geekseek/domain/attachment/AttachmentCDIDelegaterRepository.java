package org.cedj.geekseek.domain.attachment;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.attachment.model.Attachment;

@ApplicationScoped
public class AttachmentCDIDelegaterRepository implements Repository<Attachment> {

    @EJB
    private AttachmentRepository repo;

    public Class<Attachment> getType() {
        return repo.getType();
    }

    public Attachment store(Attachment entity) {
        return repo.store(entity);
    }

    public Attachment get(String id) {
        return repo.get(id);
    }

    public void remove(Attachment entity) {
        repo.remove(entity);
    }
}
