package nl.stoux.minor.access;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import nl.stoux.minor.Main;
import nl.stoux.minor.domain.Company;
import nl.stoux.minor.domain.CourseInstance;
import nl.stoux.minor.domain.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Stoux on 07/10/2015.
 */
public class StudentHandler implements AutoCloseable {

    private Connection connection;

    public StudentHandler() throws SQLException {
        connection = Main.getConnection();
    }

    public StudentHandler(Connection connection) {
        this.connection = connection;
    }

    /**
     * Find a Student by it's ID
     * @param id The id
     * @return The student
     * @throws SQLException
     */
    public Optional<Student> findStudent(int id) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("SELECT forename, surname, branch, company_id FROM students WHERE id = ?");
        prep.setInt(1, id);
        ResultSet rs = prep.executeQuery();
        if (rs.next()) {
            Optional<Company> company = findCompany(rs.getInt("company_id"));
            if (company.isPresent()) {
                return Optional.of(new Student(id, rs.getString("forename"), rs.getString("surname"), rs.getString("branch"), company.get()));
            }
        }
        return Optional.empty();
    }

    private Optional<Student> getStudent(int id) {
        try {
            return findStudent(id);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    /**
     * Insert a student into the database
     * @param student The student
     * @throws SQLException
     */
    public void insertStudent(Student student) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO students (forename, surname, branch, company_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, student.getForename());
        prep.setString(2, student.getSurname());
        prep.setString(3, student.getBranch());
        prep.setInt(4, student.getCompany().getId());
        prep.executeUpdate();
        ResultSet rs = prep.getGeneratedKeys();
        rs.next();
        int id = rs.getInt(1);
        student.setId(id);
    }

    /**
     * Get all students enrolled in a certain CourseInstance
     * @param enrolledIn
     * @return
     * @throws SQLException
     */
    public Collection<Student> getEnrolledStudents(CourseInstance enrolledIn) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("SELECT student_id FROM course_enrolment WHERE course_instance_id = ?");
        prep.setInt(1, enrolledIn.getId());
        ResultSet rs = prep.executeQuery();

        Set<Integer> studentIds = new HashSet<>();
        while(rs.next()) {
            studentIds.add(rs.getInt("student_id"));
        }

        //TODO: *very edgy* Java 8, performs horrible
        return studentIds.stream()
                .map(this::getStudent) //This call :')
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }


    /**
     * Find a company by it's ID
     * @param id the ID
     * @return The company
     * @throws SQLException
     */
    public Optional<Company> findCompany(int id) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("SELECT name, offer_number FROM companies WHERE id = ?");
        prep.setInt(1, id);
        ResultSet rs = prep.executeQuery();
        if (rs.next()) {
            return Optional.of(new Company(id, rs.getString("name"), rs.getInt("offer_number")));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Insert a company into the database
     * @param company The company
     * @throws SQLException
     */
    public void insertCompany(Company company) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO companies (name, offer_number) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, company.getName());
        prep.setInt(2, company.getOfferNumber());
        prep.executeUpdate();
        ResultSet rs = prep.getGeneratedKeys();
        rs.next();
        int id = rs.getInt(1);
        company.setId(id);
    }

    /**
     * Enroll a student in a Course instance
     * @param instance
     * @param student
     * @throws SQLException
     */
    public void enrollStudent(CourseInstance instance, Student student) throws SQLException {
        PreparedStatement prep = connection.prepareStatement("INSERT INTO course_enrolment (student_id, course_instance_id) VALUES (?,?)");
        prep.setInt(1, student.getId());
        prep.setInt(2, instance.getId());
        prep.executeUpdate();
    }

    /**
     * Get all Course instances this Student is enrolled, that start after a certain date
     * @param student The student
     * @param fromStartDate Start date (inclusive)
     * @return Map /w Course Code -> Course IDs
     * @throws SQLException
     */
    public Multimap<String, Integer> enrolledIn(Student student, LocalDate fromStartDate) throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "SELECT course_instances.id, course_instances.course_code FROM course_enrolment " +
                    "LEFT OUTER JOIN course_instances ON course_instances.id = course_enrolment.course_instance_id " +
                "WHERE course_instances.start_date >= ? AND course_enrolment.student_id = ?"
        );
        prep.setDate(1, Date.valueOf(fromStartDate));
        prep.setInt(2, student.getId());
        ResultSet rs = prep.executeQuery();

        Multimap<String, Integer> codeToIds = ArrayListMultimap.create();
        while(rs.next()) {
            codeToIds.put(rs.getString("course_instances.course_code"), rs.getInt("course_instances.id"));
        }

        return codeToIds;
    }


    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
