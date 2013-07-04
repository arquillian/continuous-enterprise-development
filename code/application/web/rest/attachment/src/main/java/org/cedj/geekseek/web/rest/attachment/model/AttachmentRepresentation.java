package org.cedj.geekseek.web.rest.attachment.model;

import java.net.URL;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;

@XmlRootElement(name = "attachment", namespace = "urn:ced:attachment")
public class AttachmentRepresentation extends LinkableRepresenatation<Attachment> {

    private Attachment attachment;

    public AttachmentRepresentation() {
        this(new Attachment(), null);
    }

    public AttachmentRepresentation(Attachment attachment, UriInfo uriInfo) {
        super(Attachment.class, "attachment", uriInfo);
        this.attachment = attachment;
    }

    @XmlElement
    public String getTitle() {
        return attachment.getTitle();
    }

    public void setTitle(String title) {
        attachment.setTitle(title);
    }

    @XmlElement
    public String getMimeType() {
        return attachment.getMimeType();
    }

    public void setMimeType(String mimeType) {
        attachment.setMimeType(mimeType);
    }

    @XmlElement
    public URL getUrl() {
        return attachment.getUrl();
    }

    public void setUrl(URL url) {
        attachment.setUrl(url);
    }

    public Attachment to() {
        return attachment;
    }
}
