package org.cedj.geekseek.service.security.resteasy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.cedj.geekseek.domain.Current;
import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.service.security.interceptor.SecurityInterceptor;
import org.jboss.resteasy.spi.DefaultOptionsMethodException;

/**
 * Generic implementation of @OPTIONS using RestEasys
 * DefaultOptionsMethodException handling.
 *
 * If a @Current User is not provided we filter the Allow header
 * and assume all state changing operations, POST, PUT, DELETE
 * require authentication to be performed.
 *
 * This does not secure the service it self, which is handled by the
 * {@link SecurityInterceptor}, but rather simply remove
 * the state changing verbs from the allowd list when someone
 * is querying the URL for it's capabilities.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 */
@Provider
public class SecuredOptionsExceptionMapper implements ExceptionMapper<DefaultOptionsMethodException> {

    private static final String HEADER_ALLOW = "Allow";
    private static List<String> securedMethods = Arrays.asList(new String[]{
        HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE});

    @Inject @Current
    private Instance<User> user;

    @Override
    public Response toResponse(DefaultOptionsMethodException exception) {
        if(user.get() == null) {
            MultivaluedMap<String, Object> values = exception.getResponse().getMetadata();
            ResponseBuilder filtered = Response.ok();
            String allow = filterAllows(values.get(HEADER_ALLOW));
            filtered.header(HEADER_ALLOW, allow);
            return filtered.build();
        }
        return exception.getResponse();
    }

    private String filterAllows(List<Object> allows) {
        Set<String> allowedAllows = new HashSet<String>();
        for(Object obj: allows) {
            String allow = String.valueOf(obj);
            String[] als = allow.split(",");
            for(String al: als) {
                String trimmed = al.trim();
                if(!securedMethods.contains(trimmed)) {
                    allowedAllows.add(trimmed);
                }
            }
        }
        return joinAllows(allowedAllows);
    }

    private String joinAllows(Set<String> allowedAllows) {
        StringBuilder sb = new StringBuilder();

        Iterator<String> iter = allowedAllows.iterator();
        while(iter.hasNext()) {
            String allow = iter.next();
            sb.append(allow);
            if(iter.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
