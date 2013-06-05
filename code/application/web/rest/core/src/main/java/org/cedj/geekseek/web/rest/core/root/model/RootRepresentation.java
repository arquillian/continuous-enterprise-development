package org.cedj.geekseek.web.rest.core.root.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;

@XmlRootElement(name = "root", namespace = "urn:ced:root")
public class RootRepresentation extends LinkableRepresenatation<Object> {

    @Override
    public Object to() {
        return null;
    }
}
