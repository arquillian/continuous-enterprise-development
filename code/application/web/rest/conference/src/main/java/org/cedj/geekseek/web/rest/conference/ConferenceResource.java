package org.cedj.geekseek.web.rest.conference;

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.web.rest.conference.model.ConferenceRepresentation;
import org.cedj.geekseek.web.rest.conference.model.SessionRepresentation;
import org.cedj.geekseek.web.rest.core.Resource;

@Path("/conference")
public class ConferenceResource implements Resource {

    private static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    private static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";
    private static final String CONFERENCE_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=conference";
    private static final String CONFERENCE_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=conference";
    private static final String SESSION_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=session";
    private static final String SESSION_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=session";

    @Context
    private UriInfo uriInfo;

    @Inject
    private Repository<Conference> repository;

    @Context
    private HttpHeaders headers;

    @Override
    public Class<? extends Resource> getResourceClass() {
        return ConferenceResource.class;
    }

    @Override
    public String getResourceMediaType() {
        return CONFERENCE_XML_MEDIA_TYPE;
    }

    // Conference

    @POST
    @Consumes({BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE})
    public Response createConference(ConferenceRepresentation conferenceRepresenttion) {
        Conference conference = conferenceRepresenttion.to();

        repository.store(conference);
        return Response.created(UriBuilder.fromResource(ConferenceResource.class)
                            .segment("{id}")
                            .build(conference.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteConference(@PathParam("id") String id) {
        Conference conference = repository.get(id);
        if(conference == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        repository.remove(conference);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces({BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE})
    public Response getConference(@PathParam("id") String id) {
        Conference conference = repository.get(id);
        if(conference == null) {
            return Response.status(Status.NOT_FOUND).type(CONFERENCE_XML_MEDIA_TYPE).build();
        }

        return Response.ok(
                new ConferenceRepresentation(conference, uriInfo.getAbsolutePathBuilder()))
                .type(getConferenceMediaType()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE})
    public Response updateConference(@PathParam("id") String id, ConferenceRepresentation conferenceRepresentation) {
        Conference conference = repository.get(id);
        if(conference == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }

        // TODO: Move to some 'converter'
        Conference updatedConference = conferenceRepresentation.to();
        conference.setName(updatedConference.getName());
        conference.setTagLine(updatedConference.getTagLine());
        conference.setDuration(updatedConference.getDuration());
        repository.store(conference);

        return Response.noContent().build();
    }

    // Session

    @GET
    @Path("/{c_id}/session")
    @Produces({BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE})
    public Response listSessions(@PathParam("c_id") String conferenceId) {
        List<SessionRepresentation> result = new ArrayList<SessionRepresentation>();
        for(Session session : repository.get(conferenceId).getSessions()) {
            result.add(new SessionRepresentation(
                    session,
                    uriInfo));
        }
        return Response.ok(result).type(getSessionMediaType()).build();
    }

    @POST
    @Path("/{c_id}/session")
    @Consumes({BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE})
    public Response createSession(@PathParam("c_id") String conferenceId, SessionRepresentation sessionRepresentation) {
        Conference conference = repository.get(conferenceId);
        if(conference == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }

        Session session = sessionRepresentation.to();
        conference.addSession(session);
        repository.store(conference);

        return Response.created(UriBuilder.fromResource(ConferenceResource.class)
                            .segment("{c_id}").segment("session").segment("{s_id}")
                            .build(conference.getId(), session.getId())).build();
    }

    @DELETE
    @Path("/{c_id}/session/{s_id}")
    public Response deleteSession(@PathParam("c_id") String conferenceId, @PathParam("s_id") String sessionId) {
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
    @Consumes({BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE})
    public Response updateSession(@PathParam("c_id") String conferenceId, @PathParam("s_id") String sessionId, SessionRepresentation sessionRepresentation) {
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
        Session updatedSession = sessionRepresentation.to();
        session.setTitle(updatedSession.getTitle());
        session.setOutline(updatedSession.getOutline());
        session.setDuration(updatedSession.getDuration());
        repository.store(conference);

        return Response.noContent().build();
    }

    @GET
    @Path("/{c_id}/session/{s_id}")
    public Response getSession(@PathParam("c_id") String conferenceId, @PathParam("s_id") String sessionId) {
        Conference conference = repository.get(conferenceId);
        if(conference == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }
        for(Session session : conference.getSessions()) {
            if(session.getId().equals(sessionId)) {
                return Response.ok(
                        new SessionRepresentation(session, uriInfo))
                        .type(getSessionMediaType()).build();
            }
        }
        return Response.status(Status.NOT_FOUND).build();
    }

    private String getConferenceMediaType() {
        return matchMediaType(CONFERENCE_XML_MEDIA_TYPE, CONFERENCE_JSON_MEDIA_TYPE);
    }

    private String getSessionMediaType() {
        return matchMediaType(SESSION_XML_MEDIA_TYPE, SESSION_JSON_MEDIA_TYPE);
    }

    private String matchMediaType(String defaultMediaType, String alternativeMediaType) {
        String selected = defaultMediaType;
        for(MediaType mt : headers.getAcceptableMediaTypes()) {
            if(mt.isCompatible(MediaType.valueOf(alternativeMediaType))) {
                selected = alternativeMediaType;
                break;
            }
        }
        return selected;
    }

}
