package org.cedj.geekseek.web.rest.core.root.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;
import org.cedj.geekseek.web.rest.core.ResourceLink;

@XmlRootElement(name = "root", namespace = "urn:ced:root")
public class RootRepresentation extends LinkableRepresenatation<ResourceLink> {

    public RootRepresentation() {
        super(ResourceLink.class);
    }

    @Override
    public ResourceLink to() {
        return null;
    }
}
