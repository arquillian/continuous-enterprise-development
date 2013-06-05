package org.cedj.geekseek.web.rest.conference.test.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "urn:ced:session")
public class Session {

    private String title;
    private String outline;

    private Date start;
    private Date end;

    public Session() {
    }

    public String getTitle() {
        return title;
    }

    public Session setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getOutline() {
        return outline;
    }

    public Session setOutline(String outline) {
        this.outline = outline;
        return this;
    }

    public Date getStart() {
        return start;
    }

    public Session setStart(Date start) {
        this.start = start;
        return this;
    }

    public Date getEnd() {
        return end;
    }

    public Session setEnd(Date end) {
        this.end = end;
        return this;
    }
}
