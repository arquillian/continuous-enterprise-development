package org.cedj.geekseek.web.rest.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.cedj.geekseek.web.rest.core.provider.JSONProvider.CustomMappingException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;

@Provider
public class JSONMappingExceptionHandler implements ExceptionMapper<CustomMappingException>{

    @Override
    public Response toResponse(CustomMappingException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(createContent(exception.getCause()))
                .build();
    }

    private Object createContent(JsonMappingException exception) {
        Map<String, String> map = new HashMap<String, String>();

        for(Reference ref : exception.getPath()) {
            map.put(ref.getFieldName(), process(exception.getMessage()));
        }
        return map;
    }

    private String process(String message) {
        if(message.indexOf("(") > -1) {
            return message.substring(0, message.indexOf("("));
        }
        return message;
    }

}
