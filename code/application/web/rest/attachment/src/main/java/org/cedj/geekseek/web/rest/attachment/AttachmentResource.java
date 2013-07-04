package org.cedj.geekseek.web.rest.attachment;

import javax.ws.rs.Path;

import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.web.rest.attachment.model.AttachmentRepresentation;
import org.cedj.geekseek.web.rest.core.RepositoryResource;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@ResourceModel
@Path("/attachment")
public class AttachmentResource extends RepositoryResource<Attachment, AttachmentRepresentation> {

    private static final String ATTACHMENT_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=attachment";
    private static final String ATTACHMENT_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=attachment";

    public AttachmentResource() {
        super(AttachmentResource.class, Attachment.class, AttachmentRepresentation.class);
    }

    @Override
    public String getResourceMediaType() {
        return ATTACHMENT_XML_MEDIA_TYPE;
    }

    @Override
    protected String[] getMediaTypes() {
        return new String[]{ATTACHMENT_XML_MEDIA_TYPE, ATTACHMENT_JSON_MEDIA_TYPE};
    }
}