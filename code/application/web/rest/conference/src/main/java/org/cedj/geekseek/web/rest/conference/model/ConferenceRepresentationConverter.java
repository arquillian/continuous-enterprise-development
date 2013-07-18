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
        ConferenceRepresentation rep = new ConferenceRepresentation(source.getId(), uriInfo);
        rep.setName(source.getName());
        rep.setTagLine(source.getTagLine());
        if(source.getDuration() != null) {
            rep.setStart(source.getDuration().getStart());
            rep.setEnd(source.getDuration().getEnd());
        }
        return rep;
    }

    @Override
    public Conference to(UriInfo uriInfo, ConferenceRepresentation representation) {
        Conference conf = new Conference(
            representation.getName(),
            representation.getTagLine(),
            new Duration(representation.getStart(), representation.getEnd()));
        return conf;
    }

    @Override
    public Conference update(UriInfo uriInfo, ConferenceRepresentation representation, Conference target) {
        target.setName(representation.getName());
        target.setTagLine(representation.getTagLine());
        target.setDuration(new Duration(representation.getStart(), representation.getEnd()));
        return target;
    }
}
