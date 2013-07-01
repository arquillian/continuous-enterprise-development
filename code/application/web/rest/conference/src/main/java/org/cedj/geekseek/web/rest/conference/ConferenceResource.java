package org.cedj.geekseek.web.rest.conference;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.web.rest.conference.model.ConferenceRepresentation;
import org.cedj.geekseek.web.rest.conference.model.SessionRepresentation;
import org.cedj.geekseek.web.rest.core.RepositoryResource;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;
import org.cedj.geekseek.web.rest.core.TopLevelResource;

@Path("/conference")
public class ConferenceResource extends RepositoryResource<Conference, ConferenceRepresentation>
    implements TopLevelResource {

    private static final String CONFERENCE_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=conference";
    private static final String CONFERENCE_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=conference";

    @Inject
    private RepresentationConverter<SessionRepresentation, Session> sessionConverter;

    @Inject
    private Repository<Conference> repository;

    @Inject
    private RepresentationConverter<ConferenceRepresentation, Conference> converter;

    @Override
    public String getResourceMediaType() {
        return CONFERENCE_XML_MEDIA_TYPE;
    }

    @Override
    protected String[] getMediaTypes() {
        return new String[]{CONFERENCE_XML_MEDIA_TYPE, CONFERENCE_JSON_MEDIA_TYPE};
    }

    @Override
    protected Repository<Conference> getRepository() {
        return repository;
    }

    @Override
    protected RepresentationConverter<ConferenceRepresentation, Conference> getConverter() {
        return converter;
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
}