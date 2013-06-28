package org.cedj.geekseek.web.rest.relation;

import static org.cedj.geekseek.web.rest.relation.Locators.locateCoverterForType;
import static org.cedj.geekseek.web.rest.relation.Locators.locateRepository;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.relation.RelationRepository;
import org.cedj.geekseek.domain.relation.model.Relation;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;

@Path("rel")
public class RelationResource {

    private static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    private static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";

    @Inject
    private RelationRepository repositry;

    @Inject
    private Instance<Repository<? extends Identifiable>> repositories;

    @Inject
    private BeanManager manager;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("{sourceObj}/{source}/{rel}/{targetObj}")
    @Produces({BASE_XML_MEDIA_TYPE, BASE_JSON_MEDIA_TYPE})
    public Response get(
        @PathParam("sourceObj") String sourceDescription,
        @PathParam("source") String sourceId,
        @PathParam("rel") String relationship,
        @PathParam("targetObj") String targetDescription) {

        Repository<? extends Identifiable> sourceRepo = locateRepository(repositories, sourceDescription);
        Repository<? extends Identifiable> targetRepo = locateRepository(repositories, targetDescription);
        if(sourceRepo == null || targetRepo == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        RepresentationConverter<Object, Object> converter = locateCoverterForType(manager, targetRepo.getType());
        if(converter == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        Identifiable source = sourceRepo.get(sourceId);
        if(source == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        List<? extends Identifiable> targets = repositry.findTargets(source, relationship, targetRepo.getType());
        if(targets == null || targets.size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok(converter.from(uriInfo, targets)).build();
    }

    @PUT
    @Path("{sourceObj}/{source}/{rel}/{targetObj}/{target}")
    @Produces({BASE_XML_MEDIA_TYPE, BASE_JSON_MEDIA_TYPE})
    public Response add(
        @PathParam("sourceObj") String sourceDescription,
        @PathParam("source") String sourceId,
        @PathParam("rel") String relationship,
        @PathParam("targetObj") String targetDescription,
        @PathParam("target") String targetId) {

        Repository<? extends Identifiable> sourceRepo = locateRepository(repositories, sourceDescription);
        Repository<? extends Identifiable> targetRepo = locateRepository(repositories, targetDescription);
        if(sourceRepo == null || targetRepo == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        Identifiable source = sourceRepo.get(sourceId);
        Identifiable target = targetRepo.get(targetId);
        if(source == null || target == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Relation relation = repositry.add(source, relationship, target);
        return Response.ok(relation).build();
    }

    @DELETE
    @Path("{sourceObj}/{source}/{rel}/{targetObj}/{target}")
    @Produces({BASE_XML_MEDIA_TYPE, BASE_JSON_MEDIA_TYPE})
    public Response remove(
        @PathParam("sourceObj") String sourceDescription,
        @PathParam("source") String sourceId,
        @PathParam("rel") String relationship,
        @PathParam("targetObj") String targetDescription,
        @PathParam("target") String targetId) {

        Repository<? extends Identifiable> sourceRepo = locateRepository(repositories, sourceDescription);
        Repository<? extends Identifiable> targetRepo = locateRepository(repositories, targetDescription);
        if(sourceRepo == null || targetRepo == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        Identifiable source = sourceRepo.get(sourceId);
        Identifiable target = targetRepo.get(targetId);
        if(source == null || target == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        repositry.remove(source, relationship, target);
        return Response.noContent().build();
    }
}
