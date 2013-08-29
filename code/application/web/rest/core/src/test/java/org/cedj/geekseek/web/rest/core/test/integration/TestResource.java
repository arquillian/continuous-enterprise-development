package org.cedj.geekseek.web.rest.core.test.integration;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.TopLevelResource;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@ResourceModel
@Path("/test")
public class TestResource implements TopLevelResource {

    private static final String REP_TYPE = "test";
    private static final String TEST_MEDIA_TYPE = "application/vnd.ced+xml;type=" + REP_TYPE;

    @Context
    private UriInfo uriInfo;

    @Override
    public Class<? extends Resource> getResourceClass() {
        return TestResource.class;
    }

    @Override
    public String getResourceMediaType() {
        return TEST_MEDIA_TYPE;
    }

    @GET
    @Produces(TEST_MEDIA_TYPE)
    public Response get() {
        return Response.ok(
            new TestRepresentation(REP_TYPE, uriInfo, new TestObject("100", "msg"))).type(TEST_MEDIA_TYPE).build();
    }

    @GET
    @Path("{id}")
    @Produces(TEST_MEDIA_TYPE)
    public Response get(@PathParam("id") String id) {
        return Response.ok(
            new TestRepresentation(REP_TYPE, uriInfo, new TestObject(id, "msg"))).type(TEST_MEDIA_TYPE).build();
    }

    @GET
    @Path("all")
    @Produces(TEST_MEDIA_TYPE)
    public Response all() {
        Collection<TestRepresentation> objects = new ArrayList<TestRepresentation>();
        objects.add(new TestRepresentation(REP_TYPE, uriInfo, new TestObject("10", "msg 10")));
        objects.add(new TestRepresentation(REP_TYPE, uriInfo, new TestObject("11", "msg 11")));

        return Response.ok(new GenericEntity<Collection<TestRepresentation>>(objects){})
                .type(TEST_MEDIA_TYPE).build();
    }
}
