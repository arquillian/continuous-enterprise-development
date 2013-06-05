package org.cedj.app.web.rest.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationMappingExceptionHandler implements ExceptionMapper<ConstraintViolationException>{

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(createContent(exception))
                .build();
    }

    private Object createContent(ConstraintViolationException exception) {
        Map<String, String> map = new HashMap<String, String>();

        for(ConstraintViolation<?> ref : exception.getConstraintViolations()) {
            map.put(ref.getPropertyPath().toString(), ref.getMessage());
        }
        return map;
    }

}
