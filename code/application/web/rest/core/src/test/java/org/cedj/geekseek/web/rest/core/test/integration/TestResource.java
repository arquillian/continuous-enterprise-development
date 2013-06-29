package org.cedj.geekseek.web.rest.core.test.integration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.cedj.geekseek.web.rest.core.Resource;

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

    @GET
    public String get() {
        return "test";
    }
}
