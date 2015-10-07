package nl.stoux.minor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Leon Stam on 5-10-2015.
 */
@Getter
@AllArgsConstructor
public class Company {

    public static final String URL = "/Companies";

    @Setter
    private int id;
    private String name;
    private int offerNumber;

    public Company(String name, int offerNumber) {
        this.name = name;
        this.offerNumber = offerNumber;
    }
}
