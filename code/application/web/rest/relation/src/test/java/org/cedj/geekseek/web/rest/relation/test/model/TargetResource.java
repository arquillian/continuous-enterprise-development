package org.cedj.geekseek.web.rest.relation.test.model;

import javax.ws.rs.Path;

import org.cedj.geekseek.domain.relation.test.model.TargetObject;
import org.cedj.geekseek.web.rest.core.MetadataResource;
import org.cedj.geekseek.web.rest.core.ResourceMetadata;
import org.cedj.geekseek.web.rest.core.ResourceMetadata.Relation;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@ResourceModel
@Path("target")
public class TargetResource implements MetadataResource {

    @Override
    public ResourceMetadata getResourceMetadata() {
        return new ResourceMetadata(TargetObject.class)
            .incoming(new Relation("connected_to"));
    }

}
