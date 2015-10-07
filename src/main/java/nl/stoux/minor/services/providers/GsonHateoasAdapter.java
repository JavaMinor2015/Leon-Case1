package nl.stoux.minor.services.providers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import nl.stoux.minor.domain.output.HateoasSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Created by Stoux on 07/10/2015.
 */
public class GsonHateoasAdapter implements JsonSerializer<HateoasSupport> {

    private static final Logger logger = LogManager.getLogger(GsonHateoasAdapter.class.getSimpleName());

    @Override
    public JsonElement serialize(HateoasSupport object, Type type, JsonSerializationContext jsonSerializationContext) {
        return serialize(object, jsonSerializationContext);
    }

    private JsonObject serialize(HateoasSupport object, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        addUrl(json, object);
        for (Field field : object.getClass().getDeclaredFields()) {
            addField(json, field, object, context);
        }
        return json;
    }

    private void addUrl(JsonObject json, HateoasSupport object) {
        Class<? extends HateoasSupport> clazz = object.getClass();
        if (UrlProvider.hasUrl(clazz)) {
            json.addProperty("url", UrlProvider.getUrl(clazz) + "/" + object.getIdentifier());
        }
    }

    private void addField(JsonObject json, Field field, HateoasSupport object, JsonSerializationContext context) {
        if (Modifier.isStatic(field.getModifiers())) {
            return;
        }

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }


        try {
            JsonElement result;
            if (object.getClass().isAssignableFrom(HateoasSupport.class)) {
                result = serialize((HateoasSupport) field.get(object), context);
            } else {
                result = context.serialize(field.get(object));
            }
            json.add(field.getName(), result);
        } catch (IllegalAccessException e) {
            //Shouldn't be thrown
            logger.warn(e);
        }
    }

}
