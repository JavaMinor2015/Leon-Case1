package nl.stoux.minor.mocking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Leon Stam on 7-10-2015.
 */
@Getter
public class RsList extends ArrayList<Map<String, Object>> {

    private boolean update;
    private int updateValue;

    public RsList(int updateValue) {
        this.update = true;
        this.updateValue = updateValue;
    }

    public RsList(Collection<? extends Map<String, Object>> c) {
        super(c);
        this.update = false;
    }

}
