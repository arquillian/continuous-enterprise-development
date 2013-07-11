package org.cedj.geekseek.web.rest.conference.test.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "urn:ced:conference", name = "conference")
public class ConferenceType {

    private String name;
    private String tagLine;

    private Date start;
    private Date end;

    public ConferenceType() {
    }

    public String getName() {
        return name;
    }

    public ConferenceType setName(String name) {
        this.name = name;
        return this;
    }

    public String getTagLine() {
        return tagLine;
    }

    public ConferenceType setTagLine(String tagLine) {
        this.tagLine = tagLine;
        return this;
    }

    public Date getStart() {
        return start;
    }

    public ConferenceType setStart(Date start) {
        this.start = start;
        return this;
    }

    public Date getEnd() {
        return end;
    }

    public ConferenceType setEnd(Date end) {
        this.end = end;
        return this;
    }
}
