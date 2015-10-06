package nl.stoux.minor.services;

import nl.stoux.minor.domain.Course;
import nl.stoux.minor.logic.CourseParser;
import nl.stoux.minor.services.base.BaseService;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Path("/courses")
public class CourseService extends BaseService {

    @POST
    @Path("/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importCourses(@FormDataParam("file") InputStream fileInputStream) {
        if (fileInputStream == null) {
            return badRequest();
        }
        return Response.ok("{\"result\":\"ok\"}", MediaType.APPLICATION_JSON_TYPE).build();
    }


}
