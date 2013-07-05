package org.cedj.geekseek.web.rest.core.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * We want to catch all Exception to avoid leaking internal state
 * out via our REST API.
 *
 * We also want to provide errors on a predictable and consumable format
 * for our clients.
 */
@Provider
public class CatchAllExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        String name = exception.getClass().getName();
        // RestEasy throws this Exception when it has to generate the default
        // behavior for an OPTIONS call.
        // To avoid having to rely on RestEasy classes compile/runtime we use
        // reflection to detect the exception type and pass on the regenerated
        // Response object.
        if(name.equals("org.jboss.resteasy.spi.DefaultOptionsMethodException")) {
            try {
                return (Response)exception.getClass().getMethod("getResponse").invoke(exception);
            } catch (Exception e) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        }
        exception.printStackTrace();
        Error error = new Error(exception.toString());
        return Response.status(Status.BAD_REQUEST).entity(error).build();
    }
}
