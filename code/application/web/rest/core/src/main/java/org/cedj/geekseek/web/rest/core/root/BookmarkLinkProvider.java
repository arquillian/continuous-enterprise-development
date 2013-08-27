package org.cedj.geekseek.web.rest.core.root;

import java.net.URI;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.web.rest.core.LinkProvider;
import org.cedj.geekseek.web.rest.core.LinkableRepresentation;
import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.ResourceLink;

public class BookmarkLinkProvider implements LinkProvider {

    @Inject
    private Instance<Resource> resources;

    @Override
    public void appendLinks(LinkableRepresentation<?> representation) {
        Resource source = getResource(representation);
        if(source == null) {
            return;
        }

        if(representation instanceof Identifiable) {
            Identifiable sourceObject = (Identifiable)representation;
            if(sourceObject.getId() != null) {
                representation.addLink(
                    generateResourceLink(
                        source,
                        sourceObject.getId(),
                        representation.getUriInfo()));
            }
        }
    }

    private Resource getResource(LinkableRepresentation<?> representation) {
        for(Resource resource: resources) {
            if(representation.getRepresentationType().equals(getResourceTypeName(resource))) {
                return resource;
            }
        }
        return null;
    }

    private ResourceLink generateResourceLink(Resource source, String id, UriInfo uriInfo) {
        UriBuilder builder = uriInfo.getBaseUriBuilder();

        URI uri = builder.path(BookmarkResource.class)
            .path(getResourceTypeName(source))
            .path(id)
            .build();

        return new ResourceLink("bookmark", uri, source.getResourceMediaType());
    }

    private String getResourceTypeName(Resource resource) {
        MediaType mediaType = MediaType.valueOf(resource.getResourceMediaType());
        return mediaType.getParameters().get("type");
    }
}
