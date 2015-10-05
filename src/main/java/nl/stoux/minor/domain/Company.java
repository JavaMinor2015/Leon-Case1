package nl.stoux.minor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
@AllArgsConstructor
public class Company {

    private int id;
    private String name;
    private int offerNumber;

}
