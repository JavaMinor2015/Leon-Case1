package nl.stoux.minor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
@AllArgsConstructor
public class CourseInstance {

    private Integer id;
    private Course course;

    /** Duration in days */
    private int duration;
    private LocalDate startDate;

    public CourseInstance(Course course, int duration, LocalDate startDate) {
        id = null;
        this.course = course;
        this.duration = duration;
        this.startDate = startDate;
    }

}
