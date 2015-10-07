package nl.stoux.minor.services;

import nl.stoux.minor.access.CourseHandler;
import nl.stoux.minor.access.StudentHandler;
import nl.stoux.minor.domain.Company;
import nl.stoux.minor.domain.Course;
import nl.stoux.minor.domain.CourseInstance;
import nl.stoux.minor.domain.Student;
import nl.stoux.minor.services.base.BaseService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Stoux on 07/10/2015.
 */
@Path(Student.URL)
public class StudentService extends BaseService {

    @GET
    @Path("/{id}")
    public Response getStudent(@PathParam("id") int studentId) {
        return get(
                StudentHandler::new,
                StudentHandler::findStudent,
                studentId
        );
    }

    @POST
    public Response insertStudent(
            @FormParam("forename") String forename,
            @FormParam("surname") String surname,
            @DefaultValue("") @FormParam("branch") String branch,
            @FormParam("company_name") String companyName,
            @FormParam("offer_number") Integer offerNumber) {
        if (offerNumber == null || !areValid(forename, surname, companyName)) {
            return badRequest();
        }

        try(StudentHandler studentHandler = new StudentHandler()) {
            Company company = new Company(companyName, offerNumber);
            studentHandler.insertCompany(company);

            Student student = new Student(forename, surname, branch, company);
            studentHandler.insertStudent(student);

            return okResponse(student);
        } catch (SQLException e) {
            logger.error(e);
            return serverError();
        }
    }

    private boolean areValid(String... strings) {
        for (String string : strings) {
            if (string == null || string.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    @GET
    @Path("/{studentId}/Courses")
    public Response getStudentsCourses(@PathParam("studentId") int studentId) {
        try (StudentHandler studentHandler = new StudentHandler(); CourseHandler courseHandler = new CourseHandler()) {
            Optional<Student> student = studentHandler.findStudent(studentId);
            if (!student.isPresent()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            List<CourseInstance> courseInstances = new ArrayList<>();
            for (Map.Entry<String, Collection<Integer>> codeToIds : studentHandler.enrolledIn(student.get(), LocalDate.now()).asMap().entrySet()) {
                Course course = courseHandler.getCourse(codeToIds.getKey()).get();
                for (Integer courseInstanceId : codeToIds.getValue()) {
                    CourseInstance instance = courseHandler.getCourseInstance(course, courseInstanceId).get();
                    courseInstances.add(instance);
                }
            }
            Collections.sort(courseInstances);

            return okResponse(asMap(
                "student", student.get(),
                "instances", courseInstances
            ));
        } catch (SQLException e) {
            logger.error(e);
            return serverError();
        }
    }

}
