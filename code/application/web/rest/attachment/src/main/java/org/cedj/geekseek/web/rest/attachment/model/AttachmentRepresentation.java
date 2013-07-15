package org.cedj.geekseek.web.rest.attachment.model;

import java.net.URL;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;

@XmlRootElement(name = "attachment", namespace = "urn:ced:attachment")
public class AttachmentRepresentation extends LinkableRepresenatation<Attachment> implements Identifiable {

    private String id;

    @NotNull
    private String title;
    @NotNull
    private String mimeType;
    @NotNull
    private URL url;

    public AttachmentRepresentation() {
        this(null, null);
    }

    public AttachmentRepresentation(String id, UriInfo uriInfo) {
        super(Attachment.class, "attachment", uriInfo);
        this.id = id;
    }

    @Override @XmlTransient
    public String getId() {
        return id;
    }

    @XmlElement
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @XmlElement
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
