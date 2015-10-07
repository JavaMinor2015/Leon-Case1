package nl.stoux.minor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.stoux.minor.domain.output.HateoasSupport;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
@AllArgsConstructor
public class Course implements HateoasSupport {

    public static final String URL = "/Courses";

    private String code;
    private String title;

    @Override
    public Object getIdentifier() {
        return code;
    }
}
