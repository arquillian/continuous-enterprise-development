package org.cedj.geekseek.web.rest.core.test.integration;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.web.rest.core.LinkableRepresentation;

@XmlRootElement(name = "test")
public class TestRepresentation extends LinkableRepresentation<TestObject> implements Identifiable {

    private TestObject object;

    // JAXB
    protected TestRepresentation() {
        this.object = new TestObject("-1", null);
    }

    public TestRepresentation(String representationType, UriInfo info, TestObject object) {
        super(TestObject.class, representationType, info);
        this.object = object;
    }

    @Override @XmlTransient
    public String getId() {
        return object.getId();
    }

    @XmlElement
    public String getMessage() {
        return object.getMessage();
    }

    public void setMessage(String message) {
        object.setMessage(message);
    }

}
