package nl.stoux.minor.access;

import com.google.common.base.Joiner;
import nl.stoux.minor.Main;
import nl.stoux.minor.domain.Course;
import nl.stoux.minor.domain.CourseInstance;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Stoux on 06/10/2015.
 */
public class CourseHandler implements AutoCloseable {

    private Connection connection;

    public CourseHandler() throws SQLException {
        this.connection = Main.getConnection();
    }

    public CourseHandler(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get a single course by it's Course code
     * @param code The code
     * @return The course optional
     * @throws SQLException
     */
    public Optional<Course> getCourse(String code) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("SELECT code, title FROM courses WHERE code = ?");
        prep.setString(1, code);
        ResultSet rs = prep.executeQuery();

        if (rs.next()) {
            return Optional.of(getCourse(rs));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get all known courses
     * @return The courses
     * @throws SQLException
     */
    public Collection<Course> getAllCourses() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT code, title FROM courses");
        List<Course> courses = new ArrayList<>();
        while(rs.next()) {
            courses.add(getCourse(rs));
        }
        return courses;
    }

    private Map<String, Course> getCourses(Collection<String> courseCodes) throws SQLException{
        Map<String, Course> map = new HashMap<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT code, title FROM courses WHERE code IN ('" + Joiner.on("', '").join(courseCodes) + "')");
        while(rs.next()) {
            map.put(rs.getString("code"), getCourse(rs));
        }
        return map;
    }

    private Course getCourse(ResultSet rs) throws SQLException {
        return new Course(rs.getString("code"), rs.getString("title"));
    }

    /**
     * Insert one or more courses into the database
     * @param courses The courses
     * @throws SQLException
     */
    public void insertCourses(Collection<Course> courses) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO courses (code, title) VALUES (?, ?)");
        for (Course course : courses) {
            prep.setString(1, course.getCode());
            prep.setString(2, course.getTitle());
            prep.addBatch();
        }
        prep.executeBatch();
    }

    /**
     * Get a single course instance
     * @param course The course
     * @param id The id
     * @return the CourseInstance
     * @throws SQLException
     */
    public Optional<CourseInstance> getCourseInstance(Course course, int id) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("SELECT id, duration, start_date FROM course_instances WHERE id = ? AND course_code = ?");
        prep.setInt(1, id);
        prep.setString(2, course.getCode());
        ResultSet rs = prep.executeQuery();

        if (rs.next()) {
            return Optional.of(getCourseInstance(course, rs));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get all CourseInstances that start between the two given dates
     * @param fromStartDate
     * @param tillStartDate
     * @return The instances
     * @throws SQLException
     */
    public Collection<CourseInstance> getCourseInstances(LocalDate fromStartDate, LocalDate tillStartDate) throws SQLException{
        List<CourseInstance> instances = new ArrayList<>();

        PreparedStatement prep = connection.prepareStatement("SELECT id, course_code, duration, start_date FROM course_instances WHERE start_date BETWEEN ? AND ?");
        prep.setDate(1, Date.valueOf(fromStartDate));
        prep.setDate(2, Date.valueOf(tillStartDate));
        ResultSet rs = prep.executeQuery();

        //Get Courses
        Set<String> courseCodes = new HashSet<>();
        while(rs.next()) {
            courseCodes.add(rs.getString("course_code"));
        }
        Map<String, Course> courses = getCourses(courseCodes);

        //Loop through results again
        rs.beforeFirst();
        while(rs.next()) {
            Course course = courses.get(rs.getString("course_code"));
            instances.add(getCourseInstance(course, rs));
        }

        return instances;
    }

    private CourseInstance getCourseInstance(Course course, ResultSet rs) throws SQLException {
        return new CourseInstance(rs.getInt("id"), course, rs.getInt("duration"), rs.getDate("start_date").toLocalDate());
    }

    /**
     * Insert one or more CourseInstances into the database
     * @param instances The course instances
     * @throws SQLException
     */
    public void insertCourseInstances(Collection<CourseInstance> instances) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO course_instances (course_code, duration, start_date) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        for (CourseInstance instance : instances) {
            prep.setString(1, instance.getCourse().getCode());
            prep.setInt(2, instance.getDuration());
            prep.setDate(3, Date.valueOf(instance.getStartDate()));
            prep.executeUpdate();
            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            instance.setId(id);
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
