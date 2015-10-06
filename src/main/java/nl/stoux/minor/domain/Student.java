package nl.stoux.minor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
@AllArgsConstructor
public class Student {

    private int id;

    private String forename;
    private String surname;

    private String branch;
    private Company company;

}
