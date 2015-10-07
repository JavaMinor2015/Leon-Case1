package nl.stoux.minor.services.models;

import nl.stoux.minor.domain.CourseInstance;
import nl.stoux.minor.domain.Student;

import java.util.Collection;

/**
 * Created by Stoux on 07/10/2015.
 */
public class CourseOverviewModel implements Comparable<CourseOverviewModel> {

    private CourseInstance courseInstance;
    private int nrOfStudents;
    private Collection<Student> students;

    public CourseOverviewModel(CourseInstance courseInstance, Collection<Student> students) {
        this.courseInstance = courseInstance;
        this.students = students;
        this.nrOfStudents = students.size();
    }

    @Override
    public int compareTo(CourseOverviewModel o) {
        return courseInstance.getStartDate().compareTo(o.courseInstance.getStartDate());
    }

}
