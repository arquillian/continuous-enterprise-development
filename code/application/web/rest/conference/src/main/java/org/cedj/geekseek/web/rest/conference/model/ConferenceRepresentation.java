package org.cedj.geekseek.web.rest.conference.model;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.web.rest.conference.ConferenceResource;
import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;
import org.cedj.geekseek.web.rest.core.ResourceLink;

@XmlRootElement(name = "conference", namespace = "urn:ced:conference")
public class ConferenceRepresentation extends LinkableRepresenatation<Conference> {

    private Conference conference;

    private Date start;
    private Date end;

    public ConferenceRepresentation() {
        this(new Conference(), null);
    }

    public ConferenceRepresentation(Conference conference, UriInfo uriInfo) {
        super(Conference.class, "conference", uriInfo);
        this.conference = conference;
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
        if (getUriInfo() != null) {
            if(doesNotContainRel("self")) {
                links.add(
                    new ResourceLink(
                        "self",
                        getUriInfo().getBaseUriBuilder().clone()
                            .path(ConferenceResource.class)
                            .segment("{id}")
                            .build(conference.getId())));
            }
            if(doesNotContainRel("session")) {
                links.add(
                    new ResourceLink(
                        "session",
                        getUriInfo().getAbsolutePathBuilder().clone()
                            .path("session")
                            .build()));
            }
        }
        return links;
    }

    public Conference to() {
        return conference;
    }
}
