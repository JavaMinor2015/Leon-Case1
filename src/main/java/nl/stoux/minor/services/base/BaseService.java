package nl.stoux.minor.services.base;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Produces("application/json")
public abstract class BaseService {


    protected Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    protected Response badRequest(Map jsonResponse) {
        return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
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

}
