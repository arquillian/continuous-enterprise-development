package org.cedj.geekseek.web.rest.relation.test.model;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.relation.test.model.SourceObject;
import org.cedj.geekseek.web.rest.core.LinkableRepresentation;

@XmlRootElement(name = "source")
public class SourceRepresentation extends LinkableRepresentation<SourceObject> implements Identifiable {

    private String id;

    public SourceRepresentation() {
        this(null, null);
    }

    public SourceRepresentation(String id, UriInfo uriInfo) {
        super(SourceObject.class, "source", uriInfo);
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
