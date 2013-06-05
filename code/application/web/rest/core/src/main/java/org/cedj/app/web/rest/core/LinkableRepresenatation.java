package org.cedj.app.web.rest.core;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public abstract class LinkableRepresenatation<X> implements Representation<X> {

    private List<ResourceLink> links;

    public LinkableRepresenatation() {
    }

    public LinkableRepresenatation(List<ResourceLink> links) {
        this.links = links;
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

}
