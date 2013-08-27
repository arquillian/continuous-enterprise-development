package org.cedj.geekseek.web.rest.core.interceptor;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
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
            linkAllRepresentations(obj);
        }
        return obj;
    }

    private boolean hasLinkableRepresentations(Object obj) {
        return locateLinkableRepresentations(obj) != null;
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

    private void linkAllRepresentations(Object obj) {
        LinkableRepresentation<?> linkable = locateLinkableRepresentations(obj);
        for(LinkProvider linker : linkProviers) {
            linker.appendLinks(linkable);
        }
    }
}
