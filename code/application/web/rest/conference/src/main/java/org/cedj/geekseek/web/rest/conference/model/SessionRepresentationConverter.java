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
        return new SessionRepresentation(source, uriInfo);
    }

    @Override
    public Session to(UriInfo uriInfo, SessionRepresentation representation) {
        return update(uriInfo, representation, new Session());
    }

    @Override
    public Session update(UriInfo uriInfo, SessionRepresentation representation, Session target) {
        target.setTitle(representation.getTitle());
        target.setOutline(representation.getOutline());
        target.setDuration(new Duration(representation.getStart(), representation.getEnd()));
        return target;
    }
}
