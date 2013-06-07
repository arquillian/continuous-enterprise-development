package org.cedj.geekseek.web.rest.conference.model;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

public class SessionRepresentationConverter extends RepresentationConverter.Base<SessionRepresentation, Session> {

    @Inject
    private Instance<UriInfo> uriInfo;

    public SessionRepresentationConverter() {
        super(SessionRepresentation.class, Session.class);
    }

    @Override
    public SessionRepresentation from(Session source) {
        return new SessionRepresentation(source, uriInfo.get());
    }

    @Override
    public Session to(SessionRepresentation representation) {
        return update(representation, new Session());
    }

    @Override
    public Session update(SessionRepresentation representation, Session target) {
        target.setTitle(representation.getTitle());
        target.setOutline(representation.getOutline());
        target.setDuration(new Duration(representation.getStart(), representation.getEnd()));
        return target;
    }
}
