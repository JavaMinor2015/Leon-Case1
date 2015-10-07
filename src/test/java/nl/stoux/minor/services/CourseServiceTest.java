package nl.stoux.minor.services;

import nl.stoux.minor.Main;
import nl.stoux.minor.TestMain;
import nl.stoux.minor.TestMixin;
import nl.stoux.minor.domain.Course;
import nl.stoux.minor.domain.CourseInstance;
import nl.stoux.minor.mocking.MockPrepStatement;
import nl.stoux.minor.mocking.MockResultSet;
import nl.stoux.minor.mocking.RsList;
import nl.stoux.minor.services.models.CourseOverviewModel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by Leon Stam on 7-10-2015.
 */
public class CourseServiceTest implements TestMixin {

    private static Connection mockConnection;
    private static CourseService service;

    @BeforeClass
    public static void setup() throws Exception {
        new TestMain().contextInitialized(null);
        mockConnection = Main.getConnection();
        service = new CourseService();
    }

    @Test
    public void getCourseInstance() throws Exception {
        //Arrange
        mockPreps(mockConnection,
            asRsList(asMap("code", "NL", "title", "Course")),
            asRsList(asMap("id", 1, "duration", 5, "start_date", Date.valueOf(LocalDate.now())))
        );

        //Act
        Response response = service.getCourseInstance("NL", 1);
        Object entity = response.getEntity();

        //Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(entity instanceof CourseInstance);
        CourseInstance instance = (CourseInstance) entity;
        assertEquals(1, instance.getId());
        assertEquals(5, instance.getDuration());
        assertEquals(LocalDate.now(), instance.getStartDate());
        assertEquals("Course", instance.getCourse().getTitle());
    }

    @Test
    public void enrolStudent() throws Exception {
        //Arrange
        mockPreps(mockConnection,
                asRsList(asMap("code", "NL", "title", "Course")),
                asRsList(asMap("id", 1, "duration", 5, "start_date", Date.valueOf(LocalDate.now()))),
                asRsList(asMap("forename", "Leon", "surname", "Stam", "branch", "", "company_id", 1)),
                asRsList(asMap("name", "Bedrijf", "offer_number", 1)),
                new RsList(0)
        );

        //Act
        Response response = service.enrolStudent("NL", 1, 1);
        Object entity = response.getEntity();

        //Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(entity instanceof Map);
        Map<Object, Object> map = (Map<Object, Object>) entity;
        assertEquals("enrolled", map.get("status"));
    }

    @Test
    public void importCourses() throws Exception {
        //TODO?
    }

    @Test
    public void importCoursesWithoutFile() throws Exception {
        //Act
        Response response = service.importCourses(null);

        //Assert
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void getOverview() throws Exception {
        RsList students = asRsList(asMap("student_id", 1), asMap("student_id", 2));
        RsList student1 = asRsList(asMap("forename", "Leon", "surname", "Stam", "branch", "", "company_id", 1));
        RsList student2 = asRsList(asMap("forename", "Henk", "surname", "Stam", "branch", "", "company_id", 1));
        RsList company = asRsList(asMap("name", "Bedrijf", "offer_number", 1));

        //Arrange
        mockPreps(
            mockConnection,
            asRsList(
                asMap("id", 3, "course_code", "NL", "duration", 5, "start_date", Date.valueOf(LocalDate.now())),
                asMap("id", 4, "course_code", "RS", "duration", 2, "start_date", Date.valueOf(LocalDate.now().minusDays(14))),
                asMap("id", 5, "course_code", "RS", "duration", 3, "start_date", Date.valueOf(LocalDate.now().minusDays(7)))
            ),
            asRsList(
                asMap("code", "NL", "title", "Nederland"),
                asMap("code", "RS", "title", "ResS")
            ),
            students, student1, company, student2, company,
            students, student1, company, student2, company,
            students, student1, company, student2, company
        );

        //Act
        Response response = service.getOverview(2013, 43);
        Object object = response.getEntity();

        //Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(object instanceof Map);
        Map map = (Map) object;
        assertEquals("/Courses/Overview/2013/44", map.get("next"));
        assertEquals("/Courses/Overview/2013/42", map.get("prev"));

        assertTrue(map.get("overview") instanceof List);
        List<CourseOverviewModel> model = (List<CourseOverviewModel>) map.get("overview");
        assertEquals(2, model.get(0).getNrOfStudents());
    }
}