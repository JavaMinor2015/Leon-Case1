package nl.stoux.minor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
@AllArgsConstructor
public class CourseInstance {

    private int id;
    private Course course;

    /** Duration in days */
    private int duration;
    private LocalDateTime startDate;

}
