package org.cedj.geekseek.web.rest.relation.test.model;

import org.cedj.geekseek.web.rest.core.RepresentationConverter;

public abstract class TestConverter<OBJ> extends RepresentationConverter.Base<OBJ, OBJ> {

    public TestConverter(Class<OBJ> type) {
        super(type, type);
    }

    @Override
    public OBJ from(OBJ source) {
        return source;
    }

    @Override
    public OBJ to(OBJ representation) {
        return representation;
    }

    @Override
    public OBJ update(OBJ representation, OBJ target) {
        return representation;
    }

}
