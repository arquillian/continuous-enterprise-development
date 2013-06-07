package org.cedj.geekseek.web.rest.relation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.model.Identifiable;
import org.cedj.geekseek.domain.relation.RelationRepository;
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
        
        Repository<? extends Identifiable> sourceRepo = locateRepository(sourceDescription);
        Repository<? extends Identifiable> targetRepo = locateRepository(targetDescription);
        
        Identifiable source = sourceRepo.get(sourceId);
        
        RepresentationConverter<Object, Object> converter = locateCoverterForType(targetRepo.getType());
        List<? extends Identifiable> targets = repositry.findTargets(source, relationship, targetRepo.getType());
        
        return Response.ok(converter.from(targets)).build();
    }

    private RepresentationConverter<Object, Object> locateCoverterForType(final Class<? extends Identifiable> type) {
        ParameterizedType paramType = new ParameterizedType() {
            @Override
            public Type getRawType() {
                return RepresentationConverter.class;
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] {Object.class, type};
            }
        };

        Set<Bean<?>> beans = manager.getBeans(paramType);
        Bean<?> bean = manager.resolve(beans);
        CreationalContext<?> cc = manager.createCreationalContext(null);

        @SuppressWarnings("unchecked")
        RepresentationConverter<Object, Object> repo = (RepresentationConverter<Object, Object>)manager.getReference(bean, paramType, cc);
        return repo;
    }

    private Repository<? extends Identifiable> locateRepository(String obj) {
        for(Repository<? extends Identifiable> repo : repositories) {
            if(repo.getType().getSimpleName().equalsIgnoreCase(obj)) {
                return repo;
            }
        }
        return null;
    }
}
