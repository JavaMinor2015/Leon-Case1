package nl.stoux.minor.services.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Produces("application/json")
public abstract class BaseService {

    protected Logger logger;

    public BaseService() {
        logger = LogManager.getLogger(getClass().getSimpleName());
    }

    protected Response okResponse(Object entity) {
        return Response.ok(entity).build();
    }

    protected Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    protected Response badRequest(String error) {
        return Response.status(Response.Status.BAD_REQUEST).entity(asMap("error", error)).build();
    }

    protected Response serverError() {
        return Response.status(500).build();
    }

    /**
     * Get multiple objects as a map for easy Json conversion
     * They will be added as Key, Value, Key, Value, etc..
     * @param objects The objects
     * @return The map
     */
    protected static Map<Object, Object> asMap(Object... objects) {
        Map<Object, Object> map = new HashMap<>();
        for (int i = 0; i < objects.length && (i + 1) < objects.length; i += 2) {
            map.put(objects[i], objects[i + 1]);
        }
        return map;
    }

    /**
     * Default get implementation for a single item identified by a single key.
     * Java 8 yay.
     * @return The response
     */
    protected <Handler extends AutoCloseable, Identifier, ReturnClass> Response get(
            HandleConstructor<Handler> constructor,
            GetFunction<Handler, Identifier, ReturnClass> getFunction,
            Identifier identifier) {
        try (Handler handler = constructor.construct()) {
            Optional<ReturnClass> result = getFunction.get(handler, identifier);
            if (result.isPresent()) {
                return okResponse(result.get());
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            logger.error(e);
            return serverError();
        }
    }

    @FunctionalInterface
    protected interface HandleConstructor<Handler extends AutoCloseable> {
        Handler construct() throws SQLException;
    }

    @FunctionalInterface
    protected interface GetFunction<Handler, Identifier, ReturnClass> {
        Optional<ReturnClass> get(Handler handler, Identifier identifier) throws SQLException;
    }

}
