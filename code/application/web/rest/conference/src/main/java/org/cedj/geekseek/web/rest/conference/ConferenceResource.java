package org.cedj.geekseek.web.rest.conference;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.web.rest.conference.model.ConferenceRepresentation;
import org.cedj.geekseek.web.rest.conference.model.SessionRepresentation;
import org.cedj.geekseek.web.rest.core.MetadataResource;
import org.cedj.geekseek.web.rest.core.RepositoryResource;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;
import org.cedj.geekseek.web.rest.core.ResourceMetadata;
import org.cedj.geekseek.web.rest.core.TopLevelResource;
import org.cedj.geekseek.web.rest.core.ResourceMetadata.NamedRelation;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@ResourceModel
@Path("/conference")
public class ConferenceResource extends RepositoryResource<Conference, ConferenceRepresentation>
    implements TopLevelResource, MetadataResource {

    public static final String CONFERENCE_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=conference";
    public static final String CONFERENCE_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=conference";

    @Inject
    private RepresentationConverter<SessionRepresentation, Session> sessionConverter;

    public ConferenceResource() {
        super(ConferenceResource.class, Conference.class, ConferenceRepresentation.class);
    }

    @Override
    public String getResourceMediaType() {
        return CONFERENCE_XML_MEDIA_TYPE;
    }

    @Override
    protected String[] getMediaTypes() {
        return new String[]{CONFERENCE_XML_MEDIA_TYPE, CONFERENCE_JSON_MEDIA_TYPE};
    }

    @Override
    public ResourceMetadata getResourceMetadata() {
        return new ResourceMetadata(Conference.class)
            .outgoing(new NamedRelation("attachments", "attached_to"))
            .outgoing(new NamedRelation("trackers", "tracked_by"))
            .outgoing(new NamedRelation("attendees", "attended_by"))
            .outgoing(new NamedRelation("locations", "located_at"));
    }

    @POST
    @Path("/{c_id}/session")
    @Consumes({ BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE })
    public Response createSession(@PathParam("c_id") String conferenceId, SessionRepresentation sessionRepresentation) {
        Conference conference = getRepository().get(conferenceId);
        if (conference == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }

        Session session = sessionConverter.to(getUriInfo(), sessionRepresentation);
        conference.addSession(session);
        getRepository().store(conference);

        return Response.created(
            UriBuilder.fromResource(
                SessionResource.class).segment("{id}")
                .build(session.getId()))
            .build();
    }

    @GET
    @Path("/{c_id}/session")
    @Produces({ BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE })
    public Response getSessions(@PathParam("c_id") String conferenceId) {
        Conference conference = getRepository().get(conferenceId);
        if (conference == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }

        Collection<SessionRepresentation> sessions = sessionConverter.from(getUriInfo(), (Collection)conference.getSessions());

        return Response.ok(new GenericEntity<Collection<SessionRepresentation>>(sessions){})
            .type(matchMediaType(
                SessionResource.SESSION_XML_MEDIA_TYPE,
                SessionResource.SESSION_JSON_MEDIA_TYPE))
            .build();
    }
}