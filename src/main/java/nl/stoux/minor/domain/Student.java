package nl.stoux.minor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.stoux.minor.domain.output.HateoasSupport;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
@AllArgsConstructor
public class Student implements HateoasSupport {

    public static final String URL = "/Students";

    @Setter
    private int id;

    private String forename;
    private String surname;

    private String branch;
    private Company company;

    public Student(String forename, String surname, String branch, Company company) {
        this.forename = forename;
        this.surname = surname;
        this.branch = branch;
        this.company = company;
    }

    @Override
    public Object getIdentifier() {
        return id;
    }

}
