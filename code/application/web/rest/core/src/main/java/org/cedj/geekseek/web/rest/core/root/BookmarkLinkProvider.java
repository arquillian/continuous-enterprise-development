package org.cedj.geekseek.web.rest.core.root;

import java.net.URI;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.web.rest.core.LinkProvider;
import org.cedj.geekseek.web.rest.core.LinkableRepresenatation;
import org.cedj.geekseek.web.rest.core.Resource;
import org.cedj.geekseek.web.rest.core.ResourceLink;

public class BookmarkLinkProvider implements LinkProvider {

    @Inject
    private Instance<Resource> resources;

    @Override
    public void appendLinks(LinkableRepresenatation<?> represenatation) {
        Resource source = getResource(represenatation);
        if(source == null) {
            return;
        }

        if(represenatation.to() instanceof Identifiable) {
            Identifiable sourceObject = (Identifiable)represenatation.to();
            represenatation.addLink(
                generateResourceLink(
                    source,
                    sourceObject,
                    represenatation.getUriInfo()));
        }
    }

    private Resource getResource(LinkableRepresenatation<?> represenatation) {
        for(Resource resource: resources) {
            if(represenatation.getRepresentationType().equals(getResourceTypeName(resource))) {
                return resource;
            }
        }
        return null;
    }

    private ResourceLink generateResourceLink(Resource source, Identifiable sourceObject, UriInfo uriInfo) {
        UriBuilder builder = uriInfo.getBaseUriBuilder();

        URI uri = builder.path(BookmarkResource.class)
            .path(getResourceTypeName(source))
            .path(sourceObject.getId())
            .build();

        return new ResourceLink("bookmark", uri, source.getResourceMediaType());
    }

    private String getResourceTypeName(Resource resource) {
        MediaType mediaType = MediaType.valueOf(resource.getResourceMediaType());
        return mediaType.getParameters().get("type");
    }
}
