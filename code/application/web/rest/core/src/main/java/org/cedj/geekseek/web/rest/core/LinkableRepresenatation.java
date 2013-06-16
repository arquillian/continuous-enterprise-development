package org.cedj.geekseek.web.rest.core;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public abstract class LinkableRepresenatation<X> implements Representation<X> {

    private List<ResourceLink> links;
    private Class<X> type;

    protected LinkableRepresenatation() {}

    public LinkableRepresenatation(Class<X> type) {
        this.type = type;
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

    @Override @XmlTransient
    public Class<X> getType() {
        return type;
    }
}
