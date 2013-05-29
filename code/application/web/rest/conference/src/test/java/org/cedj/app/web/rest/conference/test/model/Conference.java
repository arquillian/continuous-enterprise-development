package org.cedj.app.web.rest.conference.test.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "urn:ced:conference")
public class Conference {

    private String name;
    private String tagLine;

    private Date start;
    private Date end;

    public Conference() {
    }

    public String getName() {
        return name;
    }

    public Conference setName(String name) {
        this.name = name;
        return this;
    }

    public String getTagLine() {
        return tagLine;
    }

    public Conference setTagLine(String tagLine) {
        this.tagLine = tagLine;
        return this;
    }

    public Date getStart() {
        return start;
    }

    public Conference setStart(Date start) {
        this.start = start;
        return this;
    }

    public Date getEnd() {
        return end;
    }

    public Conference setEnd(Date end) {
        this.end = end;
        return this;
    }
}
