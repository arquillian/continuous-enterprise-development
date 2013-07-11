package org.cedj.geekseek.web.rest.attachment.test.model;

import java.net.URL;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "urn:ced:attachment", name = "attachment")
public class AttachmentType {

    private String title;
    private String mimeType;
    private URL url;

    public AttachmentType() {
    }

    public String getTitle() {
        return title;
    }

    public AttachmentType setTitle(String name) {
        this.title = name;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public AttachmentType setMimeType(String tagLine) {
        this.mimeType = tagLine;
        return this;
    }

    public URL getUrl() {
        return url;
    }

    public AttachmentType setUrl(URL url) {
        this.url = url;
        return this;
    }
}
