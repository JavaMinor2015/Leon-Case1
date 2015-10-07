package nl.stoux.minor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.stoux.minor.domain.output.HateoasSupport;

import java.time.LocalDate;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
@AllArgsConstructor
public class CourseInstance implements HateoasSupport {

    @Setter
    private int id;
    private Course course;

    /** Duration in days */
    private int duration;
    private LocalDate startDate;

    public CourseInstance(Course course, int duration, LocalDate startDate) {
        id = -1;
        this.course = course;
        this.duration = duration;
        this.startDate = startDate;
    }

    @Override
    public Object getIdentifier() {
        return course.getIdentifier() + "/" + id;
    }
}
