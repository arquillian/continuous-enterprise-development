package org.cedj.geekseek.web.rest.core;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public abstract class LinkableRepresentation<X> implements Representation<X> {

    private List<ResourceLink> links;
    private Class<X> sourceType;
    private String representationType;
    private UriInfo uriInfo;

    protected LinkableRepresentation() {}

    public LinkableRepresentation(Class<X> sourceType, String representationType, UriInfo uriInfo) {
        this.sourceType = sourceType;
        this.representationType = representationType;
        this.uriInfo = uriInfo;
    }

    @XmlElement(name = "link", namespace = "urn:ced:link")
    public List<ResourceLink> getLinks() {
        if (this.links == null) {
            this.links = new ArrayList<ResourceLink>();
        }
        return links;
    }

    public void addLink(ResourceLink link) {
        getLinks().add(link);
    }

    public boolean doesNotContainRel(String rel) {
        return !containRel(rel);
    }

    public boolean containRel(String rel) {
        if(links == null || links.size() == 0) {
            return false;
        }
        for(ResourceLink link : links) {
            if(rel.equals(link.getRel())) {
                return true;
            }
        }
        return false;
    }

    @Override @XmlTransient
    public Class<X> getSourceType() {
        return sourceType;
    }

    @Override @XmlTransient
    public String getRepresentationType() {
        return representationType;
    }

    @XmlTransient
    public UriInfo getUriInfo() {
        return uriInfo;
    }
}
