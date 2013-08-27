package org.cedj.geekseek.web.rest.relation;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.web.rest.core.LinkProvider;
import org.cedj.geekseek.web.rest.core.LinkableRepresentation;
import org.cedj.geekseek.web.rest.core.ResourceLink;
import org.cedj.geekseek.web.rest.relation.RelationalService.RelationMatch;

public class RelationLinkProvider implements LinkProvider {

    @Inject
    private RelationalService service;

    @Override
    public void appendLinks(LinkableRepresentation<?> representation) {
        if(!(representation instanceof Identifiable)) {
            return;
        }
        Identifiable sourceIdentifiable = (Identifiable)representation; // TODO: Remove need for cast

        Collection<RelationMatch> matches = service.getMatchingRelations(representation.getSourceType());

        for(RelationMatch target: matches) {
            UriBuilder buidler = representation.getUriInfo().getBaseUriBuilder().segment(
                                            "rel",
                                            getTypeName(target.getSourceModel()),
                                            sourceIdentifiable.getId(),
                                            target.getSource().getType(),
                                            getTypeName(target.getTargetModel()));

            // TODO: Dynamically lookup MediaType
            representation.addLink(
                new ResourceLink(
                    target.getSource().getName(),
                    buidler.build(),
                    RelationResource.BASE_JSON_MEDIA_TYPE + "; type=" + getMediaTypeName(target.getTargetModel())));
        }
    }

    private String getMediaTypeName(Class<?> type) {
        return type.getSimpleName().toLowerCase();
    }

    private String getTypeName(Class<?> type) {
        return type.getSimpleName();
    }
}
