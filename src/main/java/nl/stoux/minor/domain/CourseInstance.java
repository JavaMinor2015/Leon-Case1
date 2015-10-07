package nl.stoux.minor.domain;

import lombok.Getter;
import lombok.Setter;
import nl.stoux.minor.domain.output.HateoasSupport;

import java.time.LocalDate;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
public class CourseInstance implements HateoasSupport, Comparable<CourseInstance> {

    @Setter
    private int id;
    private Course course;

    /** Duration in days */
    private int duration;
    private LocalDate startDate;

    private double price = 0; //Lol defaults

    public CourseInstance(Course course, int duration, LocalDate startDate) {
        id = -1;
        this.course = course;
        this.duration = duration;
        this.startDate = startDate;
    }

    public CourseInstance(int id, Course course, int duration, LocalDate startDate) {
        this.id = id;
        this.course = course;
        this.duration = duration;
        this.startDate = startDate;
    }

    @Override
    public Object getIdentifier() {
        return course.getIdentifier() + "/" + id;
    }

    @Override
    public int compareTo(CourseInstance o) {
        int result = getStartDate().compareTo(o.getStartDate());
        if (result == 0) {
            return Integer.compare(getDuration(), o.getDuration());
        } else {
            return result;
        }
    }
}
