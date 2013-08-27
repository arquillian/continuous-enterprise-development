package org.cedj.geekseek.web.rest.conference.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.web.rest.conference.ConferenceResource;
import org.cedj.geekseek.web.rest.conference.SessionResource;
import org.cedj.geekseek.web.rest.core.LinkableRepresentation;
import org.cedj.geekseek.web.rest.core.ResourceLink;
import org.cedj.geekseek.web.rest.core.annotation.StartBeforeEnd;

@StartBeforeEnd
@XmlRootElement(name = "session", namespace = "urn:ced:session")
public class SessionRepresentation extends LinkableRepresentation<Session> implements Identifiable {

    private String id;
    private String parentId;

    @NotNull
    private String title;
    @NotNull
    private String outline;
    @NotNull
    private Date start;
    @NotNull
    private Date end;

    public SessionRepresentation() {
        this(null, null, null);
    }

    public SessionRepresentation(String id, String parentId, UriInfo uriInfo) {
        super(Session.class, "session", uriInfo);
        this.id = id;
        this.parentId = parentId;
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
    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    @XmlElement
    public Date getStart() {
        return start;
    }

    public void setStart(Date date) {
        start = date;
    }

    @XmlElement
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date date) {
        end = date;
    }

    public List<ResourceLink> getLinks() {
        List<ResourceLink> links = super.getLinks();
        if (getUriInfo() != null) {
            if(doesNotContainRel("self") && id != null) {
                links.add(
                    new ResourceLink(
                        "self",
                        getUriInfo().getBaseUriBuilder()
                        .path(SessionResource.class)
                        .segment("{id}")
                        .build(id)));
            }
            if(doesNotContainRel("parent") && parentId != null) {
                links.add(
                    new ResourceLink(
                        "parent",
                        getUriInfo().getBaseUriBuilder()
                        .path(ConferenceResource.class)
                        .segment("{id}")
                        .build(parentId)));
            }
        }
        return links;
    }
}
