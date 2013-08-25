package org.cedj.geekseek.web.rest.relation;

import static org.cedj.geekseek.web.rest.relation.Locators.locateCoverterForType;
import static org.cedj.geekseek.web.rest.relation.Locators.locateRepository;

import java.util.Collection;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import org.cedj.geekseek.web.rest.core.PATCH;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;
import org.cedj.geekseek.web.rest.core.ResourceLink;

@Path("rel")
public class RelationResource {

    public static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    public static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";

    @Inject
    private RelationRepository repositry;

    @Inject
    private Instance<Repository<? extends Identifiable>> repositories;

    @Inject
    private BeanManager manager;

    @Context
    private UriInfo uriInfo;

    @SuppressWarnings({ "rawtypes", "unchecked" })
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

        Collection<? extends Identifiable> targets = repositry.findTargets(source, relationship, targetRepo.getType());
        if(targets == null || targets.size() == 0) {
            return Response.noContent().build();
        }
        // Strange JVM Bug? Needs to cast to Collection for it to select the from(y, Collection<X>) method
        // Using/casting to generic Collection<X> cause it to select the from(y, X) method.
        return Response.ok(converter.from(uriInfo, (Collection)targets)).build();
    }

    @PATCH
    @Path("{sourceObj}/{source}/{rel}/{targetObj}")
    @Consumes({BASE_XML_MEDIA_TYPE, BASE_JSON_MEDIA_TYPE})
    @Produces({BASE_XML_MEDIA_TYPE, BASE_JSON_MEDIA_TYPE})
    public Response add(
        @PathParam("sourceObj") String sourceDescription,
        @PathParam("source") String sourceId,
        @PathParam("rel") String relationship,
        @PathParam("targetObj") String targetDescription,
        ResourceLink link) {

        Repository<? extends Identifiable> sourceRepo = locateRepository(repositories, sourceDescription);
        Repository<? extends Identifiable> targetRepo = locateRepository(repositories, targetDescription);
        if(sourceRepo == null || targetRepo == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        // TODO: quick and dirty
        String targetId = findLastId(link.getHref());

        Identifiable source = sourceRepo.get(sourceId);
        Identifiable target = targetRepo.get(targetId);
        if(source == null || target == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Relation relation = repositry.add(source, relationship, target);
        return Response.ok(relation).build();
    }

    @DELETE
    @Path("{sourceObj}/{source}/{rel}/{targetObj}")
    @Consumes({BASE_XML_MEDIA_TYPE, BASE_JSON_MEDIA_TYPE})
    @Produces({BASE_XML_MEDIA_TYPE, BASE_JSON_MEDIA_TYPE})
    public Response remove(
        @PathParam("sourceObj") String sourceDescription,
        @PathParam("source") String sourceId,
        @PathParam("rel") String relationship,
        @PathParam("targetObj") String targetDescription,
        ResourceLink link) {

        Repository<? extends Identifiable> sourceRepo = locateRepository(repositories, sourceDescription);
        Repository<? extends Identifiable> targetRepo = locateRepository(repositories, targetDescription);
        if(sourceRepo == null || targetRepo == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        // TODO: quick and dirty
        String targetId = findLastId(link.getHref());

        Identifiable source = sourceRepo.get(sourceId);
        Identifiable target = targetRepo.get(targetId);
        if(source == null || target == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        repositry.remove(source, relationship, target);
        return Response.noContent().build();
    }

    private String findLastId(String href) {
        int lastIndex = 0;
        if(href.endsWith("/")) {
            lastIndex = 1;
        }
        StringBuilder sb = new StringBuilder(href);
        sb.reverse();
        return sb.substring(lastIndex, sb.indexOf("/", lastIndex));
    }
}
