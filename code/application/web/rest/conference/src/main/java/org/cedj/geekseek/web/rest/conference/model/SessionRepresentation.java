package org.cedj.geekseek.web.rest.conference.model;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.web.rest.conference.ConferenceResource;
import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;
import org.cedj.geekseek.web.rest.core.ResourceLink;

@XmlRootElement(name = "session", namespace = "urn:ced:session")
public class SessionRepresentation extends LinkableRepresenatation<Session> {

    private Session session;
    private UriInfo uriInfo;

    private Date start;
    private Date end;

    public SessionRepresentation() {
        session = new Session();
    }

    public SessionRepresentation(Session session, UriInfo uriInfo) {
        this.session = session;
        this.uriInfo = uriInfo;
    }

    @XmlElement
    public String getTitle() {
        return session.getTitle();
    }

    public void setTitle(String title) {
        session.setTitle(title);
    }

    @XmlElement
    public String getOutline() {
        return session.getOutline();
    }

    public void setOutline(String outline) {
        session.setOutline(outline);
    }

    @XmlElement
    public Date getStart() {
        return session.getDuration().getStart();
    }

    public void setStart(Date date) {
        start = date;
        setDurationIfBothDatesSet();
    }

    @XmlElement
    public Date getEnd() {
        return session.getDuration().getEnd();
    }

    public void setEnd(Date date) {
        end = date;
        setDurationIfBothDatesSet();
    }

    private void setDurationIfBothDatesSet() {
        if (start != null && end != null) {
            session.setDuration(new Duration(start, end));
        }
    }

    public List<ResourceLink> getLinks() {
        List<ResourceLink> links = super.getLinks();
        if (uriInfo != null) {
            links.add(
                new ResourceLink(
                    "self",
                    uriInfo.getBaseUriBuilder()
                        .path(ConferenceResource.class)
                        .path(ConferenceResource.class, "getSession")
                        .build(session.getConference().getId(), session.getId())));
            links.add(
                new ResourceLink(
                    "parent",
                    uriInfo.getBaseUriBuilder()
                        .path(ConferenceResource.class)
                        .path(ConferenceResource.class, "getConference")
                        .build(session.getConference().getId())));
        }
        return links;
    }

    public Session to() {
        return session;
    }
}
