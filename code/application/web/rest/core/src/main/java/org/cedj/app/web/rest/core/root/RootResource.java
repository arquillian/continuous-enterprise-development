package org.cedj.app.web.rest.core.root;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.cedj.app.web.rest.core.Resource;
import org.cedj.app.web.rest.core.ResourceLink;
import org.cedj.app.web.rest.core.annotation.ResourceModel;
import org.cedj.app.web.rest.core.root.model.RootRepresentation;

@Path("/")
@ResourceModel
public class RootResource {

    private static final String BASE_MEDIA_TYPE = "application/vnd.ced+xml";
    private static final String SUB_MEDIA_TYPE = BASE_MEDIA_TYPE + ";type=root";

    @Context
    private UriInfo uriInfo;

    @Inject
    private Instance<Resource> resources;

    @GET
    @Produces(BASE_MEDIA_TYPE)
    public Response listAllResources() {

        RootRepresentation root = new RootRepresentation();
        for (Resource resource : resources) {
            root.addLink(
                    new ResourceLink(
                            toResourceName(resource.getResourceClass()),
                            uriInfo.getAbsolutePathBuilder().path(resource.getResourceClass()).build(),
                            resource.getResourceMediaType()));
        }
        return Response.ok(root).type(SUB_MEDIA_TYPE).build();
    }

    private String toResourceName(Class<?> resourceClass) {
        return resourceClass.getSimpleName().replace("Resource", "").toLowerCase();
    }
}