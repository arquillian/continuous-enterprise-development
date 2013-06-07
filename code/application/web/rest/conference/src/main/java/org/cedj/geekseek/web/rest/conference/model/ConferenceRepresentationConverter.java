package org.cedj.geekseek.web.rest.conference.model;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

@RequestScoped
public class ConferenceRepresentationConverter extends RepresentationConverter.Base<ConferenceRepresentation, Conference> {

    @Inject
    private Instance<UriInfo> uriInfo;

    public ConferenceRepresentationConverter() {
        super(ConferenceRepresentation.class, Conference.class);
    }

    @Override
    public ConferenceRepresentation from(Conference source) {
        return new ConferenceRepresentation(source, uriInfo.get().getAbsolutePathBuilder());
    }

    @Override
    public Conference to(ConferenceRepresentation representation) {
        return update(representation, new Conference());
    }

    @Override
    public Conference update(ConferenceRepresentation representation, Conference target) {
        target.setName(representation.getName());
        target.setTagLine(representation.getTagLine());
        target.setDuration(new Duration(representation.getStart(), representation.getEnd()));
        return target;
    }
}
