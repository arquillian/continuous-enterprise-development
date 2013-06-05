package org.cedj.geekseek.web.rest.core.root;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.ResourceLink;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;
import org.cedj.geekseek.web.rest.core.root.model.RootRepresentation;

@Path("/")
@ResourceModel
public class RootResource {

    private static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    private static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";
    private static final String SUB_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + ";type=root";
    private static final String SUB_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + ";type=root";

    @Context
    private UriInfo uriInfo;

    @Inject
    private Instance<Resource> resources;

    @Context
    private HttpHeaders headers;

    @GET
    @Produces({BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE})
    public Response listAllResources() {

        RootRepresentation root = new RootRepresentation();
        for (Resource resource : resources) {
            root.addLink(
                    new ResourceLink(
                            toResourceName(resource.getResourceClass()),
                            uriInfo.getAbsolutePathBuilder().path(resource.getResourceClass()).build(),
                            resource.getResourceMediaType()));
        }

        String selected = SUB_XML_MEDIA_TYPE;
        for(MediaType mt : headers.getAcceptableMediaTypes()) {
            if(mt.isCompatible(MediaType.valueOf(SUB_JSON_MEDIA_TYPE))) {
                selected = SUB_JSON_MEDIA_TYPE;
                break;
            }
        }

        return Response.ok(root).type(selected).build();
    }

    private String toResourceName(Class<?> resourceClass) {
        return resourceClass.getSimpleName().replace("Resource", "").toLowerCase();
    }
}