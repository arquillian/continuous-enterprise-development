package org.cedj.geekseek.web.rest.core;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.model.Timestampable;

public abstract class RepositoryResource<DOMAIN extends Identifiable&Timestampable, REP extends Representation<DOMAIN>>
    implements Resource {

    protected static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    protected static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";

    private Class<? extends Resource> resourceClass;
    private Class<DOMAIN> domainClass;
    private Class<REP> representationClass;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @Inject
    private Repository<DOMAIN> repository;

    @Inject
    private RepresentationConverter<REP, DOMAIN> converter;

    // for CDI proxyabillity
    protected RepositoryResource() {}

    public RepositoryResource(Class<? extends Resource> resourceClass, Class<DOMAIN> domainClass, Class<REP> representationClass) {
        this.resourceClass = resourceClass;
        this.domainClass = domainClass;
        this.representationClass = representationClass;
    }

    @Override
    public Class<? extends Resource> getResourceClass() {
        return resourceClass;
    }

    public Class<DOMAIN> getDomainClass() {
        return domainClass;
    }

    public Class<REP> getRepresentationClass() {
        return representationClass;
    }

    protected Repository<DOMAIN> getRepository() {
        return repository;
    }

    protected RepresentationConverter<REP, DOMAIN> getConverter() {
        return converter;
    }

    protected UriInfo getUriInfo() {
        return uriInfo;
    }

    @POST
    @Consumes({ BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE })
    public Response create(REP representtion) {
        DOMAIN entity = getConverter().to(uriInfo, representtion);

        getRepository().store(entity);
        return Response.created(
            UriBuilder.fromResource(getResourceClass()).segment("{id}").build(entity.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        DOMAIN entity = getRepository().get(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        getRepository().remove(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces({ BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE })
    public Response get(@PathParam("id") String id) {
        DOMAIN entity = getRepository().get(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).type(getMediaType()).build();
        }

        return Response.ok(getConverter().from(uriInfo, entity))
            .type(getMediaType())
            .lastModified(entity.getLastModified())
            .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({ BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE })
    public Response update(@PathParam("id") String id, REP representation) {
        DOMAIN entity = getRepository().get(id);
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }

        getConverter().update(uriInfo, representation, entity);
        getRepository().store(entity);

        return Response.noContent().build();
    }

    // Internal Helpers

    protected abstract String[] getMediaTypes();

    // Work around for faulty @Produces alg when using type arguments
    private String getMediaType() {
        return matchMediaType(getMediaTypes()[0], getMediaTypes()[1]);
    }

    protected String matchMediaType(String defaultMediaType, String alternativeMediaType) {
        String selected = defaultMediaType;
        for (MediaType mt : headers.getAcceptableMediaTypes()) {
            if (mt.isCompatible(MediaType.valueOf(alternativeMediaType))) {
                selected = alternativeMediaType;
                break;
            }
        }
        return selected;
    }
}
