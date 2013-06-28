package org.cedj.geekseek.web.rest.attachment;

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
import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.web.rest.attachment.model.AttachmentRepresentation;
import org.cedj.geekseek.web.rest.core.RepresentationConverter;
import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.annotation.ResourceModel;

@ResourceModel
@Path("/attachment")
public class AttachmentResource implements Resource {

    private static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    private static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";
    private static final String ATTACHMENT_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + "; type=attachment";
    private static final String ATTACHMENT_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + "; type=attachment";

    @Context
    private UriInfo uriInfo;

    @Inject
    private Repository<Attachment> repository;

    @Inject
    private RepresentationConverter<AttachmentRepresentation, Attachment> converter;

    @Context
    private HttpHeaders headers;

    @Override
    public Class<? extends Resource> getResourceClass() {
        return AttachmentResource.class;
    }

    @Override
    public String getResourceMediaType() {
        return ATTACHMENT_XML_MEDIA_TYPE;
    }

    @POST
    @Consumes({ BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE })
    public Response create(AttachmentRepresentation representtion) {
        Attachment entity = converter.to(uriInfo, representtion);

        repository.store(entity);
        return Response.created(
            UriBuilder.fromResource(AttachmentResource.class).segment("{id}").build(entity.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Attachment entity = repository.get(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        repository.remove(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces({ BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE })
    public Response get(@PathParam("id") String id) {
        Attachment entity = repository.get(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).type(ATTACHMENT_XML_MEDIA_TYPE).build();
        }

        return Response.ok(converter.from(uriInfo, entity))
            .type(getMediaType())
            .lastModified(entity.getLastModified())
            .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({ BASE_JSON_MEDIA_TYPE, BASE_XML_MEDIA_TYPE })
    public Response update(@PathParam("id") String id, AttachmentRepresentation representation) {
        Attachment entity = repository.get(id);
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build(); // TODO: Need Business Exception type to explain why?
        }

        converter.update(uriInfo, representation, entity);
        repository.store(entity);

        return Response.noContent().build();
    }

    // Internal Helpers

    // Work around for faulty @Produces alg when using type arguments
    private String getMediaType() {
        return matchMediaType(ATTACHMENT_XML_MEDIA_TYPE, ATTACHMENT_JSON_MEDIA_TYPE);
    }

    private String matchMediaType(String defaultMediaType, String alternativeMediaType) {
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