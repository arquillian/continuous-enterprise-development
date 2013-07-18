package org.cedj.geekseek.web.rest.conference.model;

import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

public class SessionRepresentationConverter extends RepresentationConverter.Base<SessionRepresentation, Session> {

    public SessionRepresentationConverter() {
        super(SessionRepresentation.class, Session.class);
    }

    @Override
    public SessionRepresentation from(UriInfo uriInfo, Session source) {
        String id = source.getId();
        String parentId = null;
        if(source.getConference() != null) {
            parentId = source.getConference().getId();
        }
        SessionRepresentation rep = new SessionRepresentation(id, parentId, uriInfo);
        rep.setTitle(source.getTitle());
        rep.setOutline(source.getOutline());
        if(source.getDuration() != null) {
            rep.setStart(source.getDuration().getStart());
            rep.setEnd(source.getDuration().getEnd());
        }
        return rep;
    }

    @Override
    public Session to(UriInfo uriInfo, SessionRepresentation representation) {
        Session session = new Session(
            representation.getTitle(),
            representation.getOutline(),
            new Duration(representation.getStart(), representation.getEnd()));
        return session;
    }

    @Override
    public Session update(UriInfo uriInfo, SessionRepresentation representation, Session target) {
        target.setTitle(representation.getTitle());
        target.setOutline(representation.getOutline());
        target.setDuration(new Duration(representation.getStart(), representation.getEnd()));
        return target;
    }
}
