package org.cedj.geekseek.web.rest.relation.test.model;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.relation.test.model.TargetObject;
import org.cedj.geekseek.web.rest.core.LinkableRepresentation;

@XmlRootElement(name = "target")
public class TargetRepresentation extends LinkableRepresentation<TargetObject> implements Identifiable {

    private String id;

    public TargetRepresentation() {
        this(null, null);
    }

    public TargetRepresentation(String id, UriInfo uriInfo) {
        super(TargetObject.class, "target", uriInfo);
        this.id = id;
    }

    @Override @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
