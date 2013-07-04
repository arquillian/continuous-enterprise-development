package org.cedj.geekseek.web.rest.core.test.integration;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;

@XmlRootElement(name = "test")
public class TestRepresentation extends LinkableRepresenatation<TestObject> {

    private TestObject object;

    // JAXB
    protected TestRepresentation() {
        this.object = new TestObject("-1", null);
    }

    public TestRepresentation(String representationType, UriInfo info, TestObject object) {
        super(TestObject.class, representationType, info);
        this.object = object;
    }

    @Override
    public TestObject to() {
        return object;
    }

    @XmlElement
    public String getMessage() {
        return object.getMessage();
    }

    public void setMessage(String message) {
        object.setMessage(message);
    }

}
