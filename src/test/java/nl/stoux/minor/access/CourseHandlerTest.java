package nl.stoux.minor.access;

import nl.stoux.minor.Main;
import nl.stoux.minor.TestMain;
import nl.stoux.minor.TestMixin;
import nl.stoux.minor.domain.Course;
import nl.stoux.minor.mocking.MockPrepStatement;
import nl.stoux.minor.mocking.MockResultSet;
import nl.stoux.minor.services.CourseService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Leon Stam on 7-10-2015.
 */
public class CourseHandlerTest implements TestMixin {

    private static Connection mockConnection;
    private static CourseHandler handler;

    @BeforeClass
    public static void setup() throws Exception {
        new TestMain().contextInitialized(null);
        mockConnection = Main.getConnection();
        handler = new CourseHandler();
    }

    @Test
    public void getCourse() throws Exception {
        //Arrange
        mockPreps(mockConnection, asRsList(asMap("code", "NL", "title", "Course")));

        //Act
        Optional<Course> optCourse = handler.getCourse("NL");

        //Assert
        assertTrue(optCourse.isPresent());
        Course course = optCourse.get();
        assertEquals("NL", course.getCode());
        assertEquals("Course", course.getTitle());
    }

    @Test
    public void getEmptyCourse() throws Exception {
        //Arrange
        PreparedStatement prep = mock(MockPrepStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(prep);
        MockResultSet rs = mock(MockResultSet.class, CALLS_REAL_METHODS);
        rs.setEntries(asRsList());
        when(prep.executeQuery()).thenReturn(rs);

        //Act
        Optional<Course> optCourse = handler.getCourse("NL");

        //Assert
        assertTrue(!optCourse.isPresent());
    }

}