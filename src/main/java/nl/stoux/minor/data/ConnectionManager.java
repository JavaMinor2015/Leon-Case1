package nl.stoux.minor.data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Leon Stam on 5-10-2015.
 */
public class ConnectionManager {

    private DataSource dataSource;

    public ConnectionManager(DataSourceProvider provider) throws SQLException {
        dataSource = provider.getDataSource();
        testConnection();
    }

    private void testConnection() throws SQLException {
        try {
            getConnection().close();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Get a connection from the datasource
     * @return the connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
