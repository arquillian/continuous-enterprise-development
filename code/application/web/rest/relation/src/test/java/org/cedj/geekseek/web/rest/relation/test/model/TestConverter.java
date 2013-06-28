package org.cedj.geekseek.web.rest.relation.test.model;

import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.web.rest.core.RepresentationConverter;

public abstract class TestConverter<OBJ> extends RepresentationConverter.Base<OBJ, OBJ> {

    public TestConverter(Class<OBJ> type) {
        super(type, type);
    }

    @Override
    public OBJ from(UriInfo uriInfo, OBJ source) {
        return source;
    }

    @Override
    public OBJ to(UriInfo uriInfo, OBJ representation) {
        return representation;
    }

    @Override
    public OBJ update(UriInfo uriInfo, OBJ representation, OBJ target) {
        return representation;
    }

}
