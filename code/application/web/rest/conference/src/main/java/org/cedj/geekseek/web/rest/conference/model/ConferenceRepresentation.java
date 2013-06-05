package org.cedj.geekseek.web.rest.conference.model;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;
import org.cedj.geekseek.web.rest.core.ResourceLink;

@XmlRootElement(name = "conference", namespace = "urn:ced:conference")
public class ConferenceRepresentation extends LinkableRepresenatation<Conference> {

    private Conference conference;
    private UriBuilder uriBuilder;

    private Date start;
    private Date end;

    public ConferenceRepresentation(Conference conference, UriBuilder uriBuilder) {
        this.conference = conference;
        this.uriBuilder = uriBuilder;
    }

    public ConferenceRepresentation() {
        this.conference = new Conference();
    }

    @XmlElement
    public String getName() {
        return conference.getName();
    }

    public void setName(String name) {
        conference.setName(name);
    }

    @XmlElement
    public String getTagLine() {
        return conference.getTagLine();
    }

    public void setTagLine(String tagLine) {
        conference.setTagLine(tagLine);
    }

    @XmlElement
    public Date getStart() {
        return conference.getDuration().getStart();
    }

    public void setStart(Date date) {
        start = date;
        setDurationIfBothDatesSet();
    }

    @XmlElement
    public Date getEnd() {
        return conference.getDuration().getEnd();
    }

    public void setEnd(Date date) {
        end = date;
        setDurationIfBothDatesSet();
    }

    private void setDurationIfBothDatesSet() {
        if (start != null && end != null) {
            conference.setDuration(new Duration(start, end));
        }
    }

    public List<ResourceLink> getLinks() {
        List<ResourceLink> links = super.getLinks();
        if (uriBuilder != null) {
            links.add(new ResourceLink("session", uriBuilder.clone().path("session").build()));
            // links.add(new ResourceLink("session2", uriBuilder.clone().path("session").build()));
        }
        return links;
    }

    public Conference to() {
        return conference;
    }
}
