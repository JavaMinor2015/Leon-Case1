package nl.stoux.minor.services;

import nl.stoux.minor.access.CourseHandler;
import nl.stoux.minor.access.StudentHandler;
import nl.stoux.minor.domain.Course;
import nl.stoux.minor.domain.CourseInstance;
import nl.stoux.minor.domain.Student;
import nl.stoux.minor.logic.CourseParser;
import nl.stoux.minor.services.base.BaseService;
import nl.stoux.minor.services.models.CourseOverviewModel;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Path(Course.URL)
public class CourseService extends BaseService {

    @GET
    @Path("/{code}")
    public Response getCourse(@PathParam("code") String code) {
        return get(
            CourseHandler::new,
            CourseHandler::getCourse,
            code
        );
    }

    @GET
    @Path("/{code}/{id}")
    public Response getCourseInstance(@PathParam("code") String code, @PathParam("id") int id) {
        try(CourseHandler handler = new CourseHandler()) {
            Optional<Course> course = handler.getCourse(code);
            if (course.isPresent()) {
                Optional<CourseInstance> instance = handler.getCourseInstance(course.get(), id);
                if (instance.isPresent()) {
                    return okResponse(instance.get());
                }
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (SQLException e) {
            logger.warn(e);
            return serverError();
        }
    }

    @POST
    @Path("/{code}/{id}/Enrol")
    public Response enrolStudent(@PathParam("code") String code, @PathParam("id") int id, @FormParam("student_id") int studentId) {
        try (CourseHandler courseHandler = new CourseHandler(); StudentHandler studentHandler = new StudentHandler()){
            Optional<Course> optCourse = courseHandler.getCourse(code);
            if (!optCourse.isPresent()) {
                return badRequest("Invalid Course Code");
            }

            Optional<CourseInstance> optInstance = courseHandler.getCourseInstance(optCourse.get(), id);
            if (!optInstance.isPresent()) {
                return badRequest("Invalid CourseInstance ID");
            }

            Optional<Student> optStudent = studentHandler.findStudent(studentId);
            if (!optStudent.isPresent()) {
                return badRequest("Invalid Student ID");
            }

            studentHandler.enrollStudent(optInstance.get(), optStudent.get());
            return okResponse(asMap(
                "status", "enrolled",
                "student", optStudent.get(),
                "in", optInstance.get()
            ));
        } catch (SQLException e) {
            logger.error(e);
            return serverError();
        }
    }


    @POST
    @Path("/ImportCourses")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importCourses(@FormDataParam("file") InputStream fileInputStream) {
        logger.info("Oh boy");
        if (fileInputStream == null) {
            return badRequest();
        }

        try {
            //Parse
            CourseHandler dataHandler = new CourseHandler();
            Collection<Course> courses = dataHandler.getAllCourses();
            CourseParser.ParseResult result = new CourseParser(fileInputStream, courses).getResult();

            logger.info("Handled: " + result.getNewCourses().size() + " | " + result.getNewInstances().size());

            //Insert result
            Collection<Course> newCourses = result.getNewCourses();
            if (!newCourses.isEmpty()) {
                dataHandler.insertCourses(newCourses);
            }

            Collection<CourseInstance> instances = result.getNewInstances();
            dataHandler.insertCourseInstances(instances);

            return okResponse(asMap(
                    "newCourses", newCourses.size(),
                    "courses", newCourses,
                    "newInstances", instances.size(),
                    "instances", instances
            ));
        } catch (ParseException e) {
            logger.debug(e);
            e.printStackTrace();
            return badRequest(e.getMessage());
        } catch (SQLException e) {
            logger.warn(e);
            return serverError();
        } catch (Exception e) {
            logger.warn(e);
            return serverError();
        }
    }


    @GET
    @Path("/Overview/{year : \\d{4}}/{week : \\d{1,2}}")
    public Response getOverview(@PathParam("year") int year, @PathParam("week") int week) {
        try (CourseHandler courseHandler = new CourseHandler(); StudentHandler studentHandler = new StudentHandler()) {
            //Parse the dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY/w/e");
            LocalDate fromDate = LocalDate.parse(String.format("%s/%s/1", year, week), formatter);
            LocalDate tillDate = fromDate.plusDays(6);


            final List<CourseOverviewModel> models = new ArrayList<>();
            Collection<CourseInstance> instances = courseHandler.getCourseInstances(fromDate, tillDate);
            for (CourseInstance instance : instances) {
                models.add(new CourseOverviewModel(instance, studentHandler.getEnrolledStudents(instance)));
            }
            Collections.sort(models);

            return okResponse(asMap(
                    "prev", asOverviewUrl(fromDate.minusDays(1), formatter),
                    "next", asOverviewUrl(tillDate.plusDays(1), formatter),
                    "overview", models
            ));
        } catch (DateTimeParseException e) {
            return badRequest("Invalid date");
        } catch (SQLException e) {
            logger.error(e);
            return serverError();
        }
    }

    private String asOverviewUrl(LocalDate date, DateTimeFormatter formatter) {
        String dateFormatted = formatter.format(date);
        return "/Courses/Overview/" + dateFormatted.substring(0, dateFormatted.length() - 2);
    }

}
