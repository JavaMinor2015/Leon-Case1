package nl.stoux.minor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
@AllArgsConstructor
public class CourseEnrolment {

    private Student student;
    private CourseInstance instance;

}
