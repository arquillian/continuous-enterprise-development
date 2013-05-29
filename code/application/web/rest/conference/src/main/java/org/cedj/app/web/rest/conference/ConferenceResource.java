package org.cedj.app.web.rest.conference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.cedj.app.domain.Repository;
import org.cedj.app.domain.conference.model.Conference;
import org.cedj.app.domain.conference.model.Session;
import org.cedj.app.web.rest.conference.model.ConferenceRepresentation;
import org.cedj.app.web.rest.conference.model.SessionRepresentation;
import org.cedj.app.web.rest.core.Resource;

@Path("/conference")
public class ConferenceResource implements Resource {

    private static final String BASE_MEDIA_TYPE = "application/vnd.ced+xml";
    private static final String CONFERENCE_MEDIA_TYPE = BASE_MEDIA_TYPE + ";type=conference";
    private static final String SESSION_MEDIA_TYPE = BASE_MEDIA_TYPE + ";type=session";

    @Context
    private UriInfo uriInfo;

    @Inject
    private Repository<Conference> repository;

    @Override
    public Class<? extends Resource> getResourceClass() {
        return ConferenceResource.class;
    }

    @Override
    public String getResourceMediaType() {
        return CONFERENCE_MEDIA_TYPE;
    }

    // Conference

    @POST
    @Consumes({"application/vnd.ced+xml"})
    public Response create(ConferenceRepresentation conferenceRepresenttion) {
        Conference conference = conferenceRepresenttion.toConference();
        repository.store(conference);
        return Response.created(UriBuilder.fromResource(ConferenceResource.class)
                            .segment("{id}")
                            .build(conference.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/vnd.ced+xml")
    public Response delete(@PathParam("id") String id) {
        Conference conference = repository.get(id);
        if(conference == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        repository.remove(conference);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/vnd.ced+xml")
    public Response get(@PathParam("id") String id) {
        Conference conference = repository.get(id);
        if(conference == null) {
            return Response.status(Status.NOT_FOUND).type(CONFERENCE_MEDIA_TYPE).build();
        }
        return Response.ok(
                new ConferenceRepresentation(conference, uriInfo.getAbsolutePathBuilder()))
                .type(CONFERENCE_MEDIA_TYPE).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/vnd.ced+xml")
    public Response update(@PathParam("id") String id, ConferenceRepresentation conferenceRepresentation) {
        Conference conference = repository.get(id);
        if(conference == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }

        // TODO: Move to some 'converter'
        Conference updatedConference = conferenceRepresentation.toConference();
        conference.setName(updatedConference.getName());
        conference.setTagLine(updatedConference.getTagLine());
        conference.setDuration(updatedConference.getDuration());
        repository.store(conference);

        return Response.noContent().build();
    }

    // Session

    @GET
    @Path("/{c_id}/session")
    @Produces("application/vnd.ced+xml")
    public Response list(@PathParam("c_id") String conferenceId) {
        List<SessionRepresentation> result = new ArrayList<SessionRepresentation>();
        for(Session session : repository.get(conferenceId).getSessions()) {
            result.add(new SessionRepresentation(
                    session,
                    uriInfo.getAbsolutePathBuilder()));
        }
        return Response.ok(result).type(SESSION_MEDIA_TYPE).build();
    }

    @POST
    @Path("/{c_id}/session")
    @Consumes("application/vnd.ced+xml")
    public Response create(@PathParam("c_id") String conferenceId, SessionRepresentation sessionRepresentation) {
        Conference conference = repository.get(conferenceId);
        if(conference == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }

        Session session = sessionRepresentation.toSession();
        conference.addSession(session);
        repository.store(conference);

        return Response.created(UriBuilder.fromResource(ConferenceResource.class)
                            .segment("{c_id}").segment("session").segment("{s_id}")
                            .build(conference.getId(), session.getId())).build();
    }

    @DELETE
    @Path("/{c_id}/session/{s_id}")
    @Produces("application/vnd.ced+xml")
    public Response delete(@PathParam("c_id") String conferenceId, @PathParam("s_id") String sessionId) {
        Conference conference = repository.get(conferenceId);
        if(conference == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }
        Session session = null;
        for(Session foundSession: conference.getSessions()) {
            if(foundSession.getId().equals(sessionId)) {
                session = foundSession;
                break;
            }
        }
        if(session == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }
        conference.removeSession(session);
        repository.store(conference);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{c_id}/session/{s_id}")
    @Consumes("application/vnd.ced+xml")
    public Response update(@PathParam("c_id") String conferenceId, @PathParam("s_id") String sessionId, SessionRepresentation sessionRepresentation) {
        Conference conference = repository.get(conferenceId);
        if(conference == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }
        Session session = null;
        for(Session foundSession: conference.getSessions()) {
            if(foundSession.getId().equals(sessionId)) {
                session = foundSession;
                break;
            }
        }
        if(session == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }

        // TODO: Move to some 'converter'
        Session updatedSession = sessionRepresentation.toSession();
        session.setTitle(updatedSession.getTitle());
        session.setOutline(updatedSession.getOutline());
        session.setDuration(updatedSession.getDuration());
        repository.store(conference);

        return Response.noContent().build();
    }

    @GET
    @Path("/{c_id}/session/{s_id}")
    public Response get(@PathParam("c_id") String conferenceId, @PathParam("s_id") String sessionId) {
        for(Session session : repository.get(conferenceId).getSessions()) {
            if(session.getId().equals(sessionId)) {
                return Response.ok(
                        new SessionRepresentation(session, uriInfo.getAbsolutePathBuilder()))
                        .type(SESSION_MEDIA_TYPE).build();
            }
        }
        return Response.status(Status.NOT_FOUND).type(SESSION_MEDIA_TYPE).build();
    }
}
