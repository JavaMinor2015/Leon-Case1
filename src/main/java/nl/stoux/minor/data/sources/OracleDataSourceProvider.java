package nl.stoux.minor.data.sources;

import nl.stoux.minor.data.DataSourceProvider;
import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by Leon Stam on 5-10-2015.
 */
public class OracleDataSourceProvider implements DataSourceProvider {

    private static final String JDBC_URL = "jdbc:oracle:thin:@%s:%s:%s";

    //TODO: Get this from a config file
    private static final String HOST = "localhost";
    private static final int PORT = 1521;
    private static final String DATABASE = "xe";
    private static final String USER = "system";
    private static final String PASSWORD = "system";


    @Override
    public DataSource getDataSource() throws SQLException {
        String connectionURL = String.format(JDBC_URL, HOST, PORT, DATABASE);
        OracleDataSource source =  new OracleDataSource();
        source.setURL(connectionURL);
        source.setUser(USER);
        source.setPassword(PASSWORD);
        return source;
    }

}
