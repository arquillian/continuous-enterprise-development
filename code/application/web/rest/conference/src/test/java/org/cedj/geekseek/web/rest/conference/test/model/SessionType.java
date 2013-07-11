package org.cedj.geekseek.web.rest.conference.test.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "urn:ced:session", name = "session")
public class SessionType {

    private String title;
    private String outline;

    private Date start;
    private Date end;

    public SessionType() {
    }

    public String getTitle() {
        return title;
    }

    public SessionType setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getOutline() {
        return outline;
    }

    public SessionType setOutline(String outline) {
        this.outline = outline;
        return this;
    }

    public Date getStart() {
        return start;
    }

    public SessionType setStart(Date start) {
        this.start = start;
        return this;
    }

    public Date getEnd() {
        return end;
    }

    public SessionType setEnd(Date end) {
        this.end = end;
        return this;
    }
}
