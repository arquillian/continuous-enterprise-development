package org.cedj.geekseek.web.rest.conference.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.web.rest.conference.ConferenceResource;
import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;
import org.cedj.geekseek.web.rest.core.ResourceLink;
import org.cedj.geekseek.web.rest.core.annotation.StartBeforeEnd;

@StartBeforeEnd
@XmlRootElement(name = "conference", namespace = "urn:ced:conference")
public class ConferenceRepresentation extends LinkableRepresenatation<Conference> implements Identifiable {

    private String id;
    @NotNull
    private String name;
    @NotNull
    private String tagLine;
    @NotNull
    private Date start;
    @NotNull
    private Date end;

    public ConferenceRepresentation() {
        this(null, null);
    }

    public ConferenceRepresentation(String id, UriInfo uriInfo) {
        super(Conference.class, "conference", uriInfo);
        this.id = id;
    }

    @Override @XmlTransient
    public String getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
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
                        getUriInfo().getBaseUriBuilder().clone()
                            .path(ConferenceResource.class)
                            .segment("{id}")
                            .build(id)));
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
}
