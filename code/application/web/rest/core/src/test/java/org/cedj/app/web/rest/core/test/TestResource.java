package org.cedj.app.web.rest.core.test;

import javax.ws.rs.Path;

import org.cedj.app.web.rest.core.Resource;

@Path("/test")
public class TestResource implements Resource {

    @Override
    public Class<? extends Resource> getResourceClass() {
        return TestResource.class;
    }

    @Override
    public String getResourceMediaType() {
        return "urn:ced:test";
    }
}
