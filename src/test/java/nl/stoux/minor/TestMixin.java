package nl.stoux.minor;

import nl.stoux.minor.mocking.MockPrepStatement;
import nl.stoux.minor.mocking.MockResultSet;
import nl.stoux.minor.mocking.RsList;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Stoux on 05/10/2015.
 *
 * Let's try something new by using Mix-in's... Sorta.
 */
public interface TestMixin {

    default InputStream getTestResource(String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }

    default RsList asRsList(Map<String, Object>... objects) {
        return new RsList(Stream.of(objects).collect(Collectors.toList()));
    }

    default Map<String, Object> asMap(Object... objects) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < objects.length && (i + 1) < objects.length; i += 2) {
            if (!(objects[i] instanceof String)) {
                continue;
            }
            map.put((String) objects[i], objects[i + 1]);
        }
        return map;
    }

    default void mockPreps(Connection connection, RsList... returns) throws SQLException {
        List<PreparedStatement> prepList = new ArrayList<>();
        for (RsList list : returns) {
            PreparedStatement preparedStatement = mock(MockPrepStatement.class);
            if (list.isUpdate()) {
                when(preparedStatement.executeUpdate()).thenReturn(list.getUpdateValue());
            } else {
                MockResultSet rs = mock(MockResultSet.class, CALLS_REAL_METHODS);
                rs.setEntries(list);
                when(preparedStatement.executeQuery()).thenReturn(rs);
            }

            prepList.add(preparedStatement);
        }

        PreparedStatement first = prepList.get(0);
        prepList.remove(0);
        when(connection.prepareStatement(any())).thenReturn(first, prepList.toArray(new PreparedStatement[prepList.size()]));
    }

}
