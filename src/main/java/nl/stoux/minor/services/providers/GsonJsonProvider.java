package nl.stoux.minor.services.providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import nl.stoux.minor.domain.output.HateoasSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by Leon Stam on 5-10-2015.
 *
 * Class that enables the web application to provide Json responses.
 * This turns Response entities into Json code using Gson.
 */
@Provider
@Consumes({"application/json", "text/json"})
@Produces({"application/json", "text/json"})
public class GsonJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

    private static final String UTF8 = "UTF-8";

    @Getter
    private static Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(HateoasSupport.class, new GsonHateoasAdapter())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    private static Logger logger = LogManager.getLogger(GsonJsonProvider.class.getSimpleName());

    @Override
    public void writeTo(Object o, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        try (OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream)) {
            gson.toJson(o, type, streamWriter);
        }
    }

    @Override
    public Object readFrom(Class<Object> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        try (InputStreamReader streamReader = new InputStreamReader(inputStream, UTF8)) {
            return gson.fromJson(streamReader, type);
        } catch (JsonSyntaxException e) {
            logger.warn(e);
        }
        return null;
    }

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return  true;
    }

    @Override
    public long getSize(Object o, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

}
