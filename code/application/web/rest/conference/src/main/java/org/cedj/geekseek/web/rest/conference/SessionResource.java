package org.cedj.geekseek.web.rest.conference;

import javax.ws.rs.Path;

import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.web.rest.conference.model.SessionRepresentation;
import org.cedj.geekseek.web.rest.core.MetadataResource;
import org.cedj.geekseek.web.rest.core.RepositoryResource;
import org.cedj.geekseek.web.rest.core.ResourceMetadata;
import org.cedj.geekseek.web.rest.core.ResourceMetadata.NamedRelation;
import org.cedj.geekseek.web.rest.core.ResourceMetadata.Relation;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@ResourceModel
@Path("/session")
public class SessionResource extends RepositoryResource<Session, SessionRepresentation> implements MetadataResource {

    public static final String SESSION_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=session";
    public static final String SESSION_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=session";


    public SessionResource() {
        super(SessionResource.class, Session.class, SessionRepresentation.class);
    }

    @Override
    public String getResourceMediaType() {
        return SESSION_XML_MEDIA_TYPE;
    }

    @Override
    protected String[] getMediaTypes() {
        return new String[] {SESSION_XML_MEDIA_TYPE, SESSION_JSON_MEDIA_TYPE};
    }

    @Override
    public ResourceMetadata getResourceMetadata() {
        return new ResourceMetadata(Session.class)
            .incoming(new Relation("presented_by"))
            .outgoing(new NamedRelation("attachments", "attached_to"))
            .outgoing(new NamedRelation("speakers", "presented_by"))
            .outgoing(new NamedRelation("attendees", "attended_by"))
            .outgoing(new NamedRelation("locations", "located_in"));
    }
}