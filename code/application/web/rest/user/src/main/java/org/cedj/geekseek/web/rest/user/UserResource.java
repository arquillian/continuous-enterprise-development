package org.cedj.geekseek.web.rest.user;

import javax.ws.rs.Path;

import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.web.rest.core.MetadataResource;
import org.cedj.geekseek.web.rest.core.RepositoryResource;
import org.cedj.geekseek.web.rest.core.ResourceMetadata;
import org.cedj.geekseek.web.rest.core.ResourceMetadata.NamedRelation;
import org.cedj.geekseek.web.rest.core.ResourceMetadata.Relation;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;
import org.cedj.geekseek.web.rest.user.model.UserRepresentation;

@ResourceModel
@Path("/user")
public class UserResource extends RepositoryResource<User, UserRepresentation> implements MetadataResource {

    public static final String USER_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=user";
    public static final String USER_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=user";

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

    @Override
    public ResourceMetadata getResourceMetadata() {
        return new ResourceMetadata(User.class)
            .incoming(new Relation("presented_by"))
            .incoming(new Relation("tracked_by"))
            .incoming(new Relation("attended_by"))
            .outgoing(new NamedRelation("attachments", "attached_to"))
            .outgoing(new NamedRelation("sessions", "presented_by"));
    }
}