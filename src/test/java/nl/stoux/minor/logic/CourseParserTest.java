package nl.stoux.minor.logic;

import nl.stoux.minor.TestMixin;
import nl.stoux.minor.domain.Course;
import nl.stoux.minor.domain.CourseInstance;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Leon Stam on 5-10-2015.
 */
public class CourseParserTest implements TestMixin {

    private static final String CORRECT = "correct.txt";
    private static final String EXTRA_LINE = "extra_line.txt";

    @Before
    public void setup() {

    }

    private List<Course> getCourses() {
        return new ArrayList<>(Arrays.asList(
                new Course("CNETIN", "C# Programmeren"),
                new Course("ADCSB", "Advanced C#")
        ));
    }


    @Test
    public void correctWithExistingCourses() throws Exception {
        //Arrange
        List<Course> courses = getCourses();
        InputStream input = getTextResource(CORRECT);

        //Act
        CourseParser.ParseResult result = new CourseParser(input, courses).getResult();
        Collection<Course> newCourses = result.getNewCourses();
        Collection<CourseInstance> newInstances = result.getNewInstances();

        //Assert
        assertEquals(5, newInstances.size());
        assertEquals(0, newCourses.size());

        final Course cnetin = courses.get(0);
        long cnetinInstances = newInstances.stream().filter(i -> i.getCourse() == cnetin).count();
        assertEquals(3, cnetinInstances);
    }

    @Test
    public void correctWithNewCourses() throws Exception {
        //Arrange
        List<Course> courses = getCourses();
        courses.remove(0);
        InputStream input = getTextResource(CORRECT);

        //Act
        CourseParser.ParseResult result = new CourseParser(input, courses).getResult();
        Collection<Course> newCourses = result.getNewCourses();
        Collection<CourseInstance> newInstances = result.getNewInstances();

        //Assert
        assertEquals(5, newInstances.size());
        assertEquals(1, newCourses.size());

        final Course cnetin = newCourses.iterator().next();
        long cnetinInstances = newInstances.stream().filter(i -> i.getCourse() == cnetin).count();
        assertEquals(3, cnetinInstances);
    }

    @Test
    public void correctExtraLineHandler() throws Exception {
        //Arrange
        InputStream input = getTextResource(EXTRA_LINE);

        //Act
        CourseParser.ParseResult result = new CourseParser(input, new HashSet<>()).getResult();

        //Assert
        assertEquals(result.getNewCourses().size(), 1);
        assertEquals(result.getNewInstances().size(), 1);
    }


    private InputStream getTextResource(String file) {
        return getTestResource("CourseParser/" + file);
    }


}