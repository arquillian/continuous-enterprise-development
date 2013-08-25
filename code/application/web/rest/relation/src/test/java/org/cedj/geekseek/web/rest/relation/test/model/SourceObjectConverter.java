package org.cedj.geekseek.web.rest.relation.test.model;

import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.relation.test.model.SourceObject;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

public class SourceObjectConverter extends RepresentationConverter.Base<SourceRepresentation, SourceObject> {

    public SourceObjectConverter() {
        super(SourceRepresentation.class, SourceObject.class);
    }

    @Override
    public SourceRepresentation from(UriInfo uriInfo, SourceObject source) {
        return new SourceRepresentation(source.getId(), uriInfo);
    }

    @Override
    public SourceObject to(UriInfo uriInfo, SourceRepresentation representation) {
        return new SourceObject(representation.getId());
    }

    @Override
    public SourceObject update(UriInfo uriInfo, SourceRepresentation representation, SourceObject target) {
        return target;
    }
}
