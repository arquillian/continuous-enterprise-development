package org.cedj.geekseek.service.security.test.model;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.cedj.geekseek.web.rest.core.PATCH;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@ResourceModel
@Path("test")
public class TestResource {

    @GET
    public Response get() {
        return Response.ok().build();
    }

    @PUT
    public Response put() {
        return Response.ok().build();
    }

    @POST
    public Response post() {
        return Response.ok().build();
    }

    @DELETE
    public Response delete() {
        return Response.ok().build();
    }

    @PATCH
    public Response patch() {
        return Response.ok().build();
    }

}
