package org.cedj.geekseek.web.rest.core.root;

import java.net.URI;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@Path("/bookmark")
@ResourceModel
public class BookmarkResource {

    @Inject
    private Instance<Resource> resources;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{type}/{id}")
    public Response get(@PathParam("type") String type, @PathParam("id") String id) {
        Resource resource = locateResource(type);
        if(resource == null) {
            return Response.status(404).build();
        }

        URI uri = uriInfo.getBaseUriBuilder()
                .path(resource.getResourceClass())
                .path(id)
                .build();
        return Response.temporaryRedirect(uri).build();
    }

    private Resource locateResource(String type) {
        for(Resource resource : resources) {
            MediaType media = MediaType.valueOf(resource.getResourceMediaType());
            if(type.equalsIgnoreCase(media.getParameters().get("type"))) {
                return resource;
            }
        }
        return null;
    }
}
