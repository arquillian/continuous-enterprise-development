package org.cedj.geekseek.web.rest.conference.model;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

@RequestScoped
public class ConferenceRepresentationConverter extends RepresentationConverter.Base<ConferenceRepresentation, Conference> {

    public ConferenceRepresentationConverter() {
        super(ConferenceRepresentation.class, Conference.class);
    }

    @Override
    public ConferenceRepresentation from(UriInfo uriInfo, Conference source) {
        return new ConferenceRepresentation(source, uriInfo.getAbsolutePathBuilder());
    }

    @Override
    public Conference to(UriInfo uriInfo, ConferenceRepresentation representation) {
        return update(uriInfo, representation, new Conference());
    }

    @Override
    public Conference update(UriInfo uriInfo, ConferenceRepresentation representation, Conference target) {
        target.setName(representation.getName());
        target.setTagLine(representation.getTagLine());
        target.setDuration(new Duration(representation.getStart(), representation.getEnd()));
        return target;
    }
}
