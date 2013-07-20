package org.cedj.geekseek.service.security.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.cedj.geekseek.domain.Current;
import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.TopLevelResource;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;
import org.cedj.geekseek.web.rest.user.UserResource;

@RequestScoped
@ResourceModel
@Path("/security/whoami")
public class WhoAmIResource implements TopLevelResource {

    private static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    private static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";

    private static final String USER_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=user";
    private static final String USER_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=user";

    @Inject @Current
    private User user;

    @Override
    public Class<? extends Resource> getResourceClass() {
        return WhoAmIResource.class;
    }

    @Override
    public String getResourceMediaType() {
        return USER_XML_MEDIA_TYPE;
    }

    @GET
    @Produces({BASE_XML_MEDIA_TYPE, BASE_JSON_MEDIA_TYPE})
    public Response whoami() {
        if(user == null) {
            return Response.status(Status.UNAUTHORIZED).build();
        }
        String userId = user.getId();
        return Response.seeOther(
                UriBuilder.fromResource(UserResource.class).segment(userId).build())
            .build();
    }
}
