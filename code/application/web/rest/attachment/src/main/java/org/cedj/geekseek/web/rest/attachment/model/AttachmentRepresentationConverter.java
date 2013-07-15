package org.cedj.geekseek.web.rest.attachment.model;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

@RequestScoped
public class AttachmentRepresentationConverter extends RepresentationConverter.Base<AttachmentRepresentation, Attachment> {

    public AttachmentRepresentationConverter() {
        super(AttachmentRepresentation.class, Attachment.class);
    }

    @Override
    public AttachmentRepresentation from(UriInfo uriInfo, Attachment source) {
        AttachmentRepresentation rep = new AttachmentRepresentation(source.getId(), uriInfo);
        rep.setTitle(source.getTitle());
        rep.setMimeType(source.getMimeType());
        rep.setUrl(source.getUrl());
        return rep;
    }

    @Override
    public Attachment to(UriInfo uriInfo, AttachmentRepresentation representation) {
        return update(uriInfo, representation, new Attachment());
    }

    @Override
    public Attachment update(UriInfo uriInfo, AttachmentRepresentation representation, Attachment target) {
        target.setTitle(representation.getTitle());
        target.setMimeType(representation.getMimeType());
        target.setUrl(representation.getUrl());
        return target;
    }
}
