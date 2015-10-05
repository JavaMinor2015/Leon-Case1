package nl.stoux.minor.services;

import nl.stoux.minor.logic.CourseParser;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Produces("application/json")
@Path("/courses")
public class CourseService {

    @POST
    @Path("/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importCourses(@FormDataParam("file") InputStream fileInputStream) {
        return Response.ok("{\"result\":\"ok\"}", MediaType.APPLICATION_JSON_TYPE).build();
    }


}
