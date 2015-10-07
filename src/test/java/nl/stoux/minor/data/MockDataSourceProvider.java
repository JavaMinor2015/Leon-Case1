package nl.stoux.minor.data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

/**
 * Created by Leon Stam on 7-10-2015.
 */
public class MockDataSourceProvider implements DataSourceProvider {

    @Override
    public DataSource getDataSource() throws SQLException {
        DataSource mock = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        when(mock.getConnection()).thenReturn(connection);
        return mock;
    }

}
