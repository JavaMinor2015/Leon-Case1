package nl.stoux.minor.access;

import nl.stoux.minor.Main;
import nl.stoux.minor.TestMain;
import nl.stoux.minor.TestMixin;
import nl.stoux.minor.domain.Company;
import nl.stoux.minor.domain.Student;
import nl.stoux.minor.mocking.MockPrepStatement;
import nl.stoux.minor.mocking.MockResultSet;
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
public class StudentHandlerTest implements TestMixin {

    private static Connection mockConnection;
    private static StudentHandler handler;

    @BeforeClass
    public static void setup() throws Exception {
        new TestMain().contextInitialized(null);
        mockConnection = Main.getConnection();
        handler = new StudentHandler();
    }

    @Test
    public void findStudent() throws Exception {
        //Arrange
        mockPreps(mockConnection,
                asRsList(asMap("forename", "Leon", "surname", "Stam", "branch", "", "company_id", 1)),
                asRsList(asMap("name", "Bedrijf", "offer_number", 1))
        );

        //Act
        Optional<Student> optStudent = handler.findStudent(1);

        //Assert
        assertTrue(optStudent.isPresent());
        Student student = optStudent.get();
        assertEquals("Leon", student.getForename());
        assertEquals("Stam", student.getSurname());
        assertEquals("", student.getBranch());
        Company company = student.getCompany();
        assertEquals(1, company.getId());
        assertEquals(1, company.getOfferNumber());
        assertEquals("Bedrijf", company.getName());
    }

}