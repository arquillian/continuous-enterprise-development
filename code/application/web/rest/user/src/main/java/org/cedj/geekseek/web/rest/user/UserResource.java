package org.cedj.geekseek.web.rest.user;

import javax.ws.rs.Path;

import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.web.rest.core.RepositoryResource;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;
import org.cedj.geekseek.web.rest.user.model.UserRepresentation;

@ResourceModel
@Path("/user")
public class UserResource extends RepositoryResource<User, UserRepresentation> {

    private static final String USER_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=user";
    private static final String USER_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=user";

    public UserResource() {
        super(UserResource.class, User.class, UserRepresentation.class);
    }

    @Override
    public String getResourceMediaType() {
        return USER_XML_MEDIA_TYPE;
    }

    @Override
    protected String[] getMediaTypes() {
        return new String[]{USER_XML_MEDIA_TYPE, USER_JSON_MEDIA_TYPE};
    }
}