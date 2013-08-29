package org.cedj.geekseek.web.rest.core.interceptor;

import java.util.Collection;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.cedj.geekseek.web.rest.core.LinkProvider;
import org.cedj.geekseek.web.rest.core.LinkableRepresentation;

public class LinkedInterceptor implements RESTInterceptor {

    @Inject
    private Instance<LinkProvider> linkProviers;

    @Override
    public int getPriority() {
        return -10;
    }

    @Override
    public Object invoke(InvocationContext ic) throws Exception {
        Object obj = ic.proceed();
        if(hasLinkableRepresentations(obj)) {
            linkRepresentations(obj);
        } else if(hasListLinkableRepresentations(obj)) {
            linkAllRepresentations(obj);
        }
        return obj;
    }

    private boolean hasLinkableRepresentations(Object obj) {
        return locateLinkableRepresentations(obj) != null;
    }

    private boolean hasListLinkableRepresentations(Object obj) {
        return locateLinkableListRepresentations(obj) != null;
    }

    private LinkableRepresentation<?> locateLinkableRepresentations(Object obj) {
        if(obj instanceof Response) {
            Object entity = ((Response)obj).getEntity();
            if(entity instanceof LinkableRepresentation) {
                return (LinkableRepresentation<?>)entity;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Collection<LinkableRepresentation<?>> locateLinkableListRepresentations(Object obj) {
        if(obj instanceof Response) {
            Object entity = ((Response)obj).getEntity();
            if(entity instanceof Collection) {
                Collection<?> objCollection = (Collection<?>)entity;
                if(objCollection.size() > 0 && objCollection.iterator().next() instanceof LinkableRepresentation) {
                    return (Collection<LinkableRepresentation<?>>)objCollection;
                }
            } else if(entity instanceof GenericEntity) {
                GenericEntity<?> genericEntity = (GenericEntity<?>)entity;
                if(genericEntity.getEntity() instanceof Collection) {
                    Collection<?> objCollection = (Collection<?>)genericEntity.getEntity();
                    if(objCollection.size() > 0 && objCollection.iterator().next() instanceof LinkableRepresentation) {
                        return (Collection<LinkableRepresentation<?>>)objCollection;
                    }
                }
            }
        }
        return null;
    }

    private void linkRepresentations(Object obj) {
        LinkableRepresentation<?> linkable = locateLinkableRepresentations(obj);
        link(linkable);
    }

    private void linkAllRepresentations(Object obj) {
        Collection<LinkableRepresentation<?>> linkables = locateLinkableListRepresentations(obj);
        for(LinkableRepresentation<?> linkable : linkables) {
            link(linkable);
        }
    }

    private void link(LinkableRepresentation<?> linkable) {
        for(LinkProvider linker : linkProviers) {
            linker.appendLinks(linkable);
        }
    }
}
