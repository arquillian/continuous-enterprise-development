package org.cedj.geekseek.web.rest.relation.test.model;

import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.relation.test.model.TargetObject;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

public class TargetObjectConverter extends RepresentationConverter.Base<TargetRepresentation, TargetObject> {

    public TargetObjectConverter() {
        super(TargetRepresentation.class, TargetObject.class);
    }

    @Override
    public TargetRepresentation from(UriInfo uriInfo, TargetObject source) {
        return new TargetRepresentation(source.getId(), uriInfo);
    }

    @Override
    public TargetObject to(UriInfo uriInfo, TargetRepresentation representation) {
        return new TargetObject(representation.getId());
    }

    @Override
    public TargetObject update(UriInfo uriInfo, TargetRepresentation representation, TargetObject target) {
        return target;
    }
}
