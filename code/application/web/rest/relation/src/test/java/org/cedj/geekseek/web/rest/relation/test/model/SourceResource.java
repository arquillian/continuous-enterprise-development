package org.cedj.geekseek.web.rest.relation.test.model;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.relation.test.model.SourceObject;
import org.cedj.geekseek.web.rest.core.MetadataResource;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;
import org.cedj.geekseek.web.rest.core.ResourceMetadata;
import org.cedj.geekseek.web.rest.core.ResourceMetadata.NamedRelation;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@ResourceModel
@Path("source")
public class SourceResource implements MetadataResource {

    private static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    private static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";

    @Inject
    private Repository<SourceObject> repo;

    @Inject
    private RepresentationConverter<SourceRepresentation, SourceObject> converter;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @Produces({BASE_XML_MEDIA_TYPE, BASE_JSON_MEDIA_TYPE})
    public Response get(@PathParam("id") String id) {
        return Response.ok(converter.from(uriInfo, repo.get(id))).build();
    }

    @Override
    public ResourceMetadata getResourceMetadata() {
        return new ResourceMetadata(SourceObject.class)
            .outgoing(new NamedRelation("connections", "connected_to"))
            .outgoing(new NamedRelation("notdeployed", "no_matching_relation"));
    }

}
