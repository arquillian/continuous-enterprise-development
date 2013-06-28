package org.cedj.geekseek.web.rest.attachment.test.model;

import java.net.URL;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "urn:ced:attachment")
public class Attachment {

    private String title;
    private String mimeType;
    private URL url;

    public Attachment() {
    }

    public String getTitle() {
        return title;
    }

    public Attachment setTitle(String name) {
        this.title = name;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Attachment setMimeType(String tagLine) {
        this.mimeType = tagLine;
        return this;
    }

    public URL getUrl() {
        return url;
    }

    public Attachment setUrl(URL url) {
        this.url = url;
        return this;
    }
}
