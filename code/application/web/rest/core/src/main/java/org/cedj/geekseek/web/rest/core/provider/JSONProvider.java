package org.cedj.geekseek.web.rest.core.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * Provide a MessageBodyReader/Writer with a more specific MediaType then
 * the ones provided default by the JacksonJaxbJsonProvider. This is due to
 * how the application level vs build-in providers are resolved in a container.
 *
 * e.g. 'application/*+json' is more specific then * / * so the
 * JacksonJaxbProvider will never be invoked.
 *
 * Since Jackson already provide a ExceptionMapper<JsonMappingException> and
 * JAX-RS does not support @Consumes/@Produces MediaType filtering on ExceptionMapper<T>,
 * it's a bit random which handler will handle the JsonMappingException thrown when we attempt
 * to Map it as well.
 *
 * As a workaround we override the readFrom/writeTo methods provided by the
 * JacksonJaxbJsonProvider to catch the JsonMappingException and wrap it in our own
 * CustomMappingException which we can handle in our own ExceptionMapping<CustomMappingException>
 */
@Provider
@Consumes("application/vnd.ced+json")
@Produces("application/vnd.ced+json")
public class JSONProvider extends JacksonJaxbJsonProvider {

    public JSONProvider() {
        super();
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
        try {
            return super.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream);
        } catch(JsonMappingException e) {
            throw new CustomMappingException(e);
        }
    }

    @Override
    public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        try {
            super.writeTo(value, type, genericType, annotations, mediaType, httpHeaders, entityStream);
        } catch(JsonMappingException e) {
            throw new CustomMappingException(e);
        }
    }

    public static class CustomMappingException extends IOException {
        private static final long serialVersionUID = 1L;

        public CustomMappingException(JsonMappingException cause) {
            super(cause);
        }

        public JsonMappingException getCause() {
            return (JsonMappingException)super.getCause();
        }
    }
}
