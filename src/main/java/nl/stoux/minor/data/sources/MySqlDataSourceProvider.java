package nl.stoux.minor.data.sources;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import nl.stoux.minor.data.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by Stoux on 06/10/2015.
 */
public class MySqlDataSourceProvider implements DataSourceProvider {

    private static final String JDBC_URL = "jdbc:mysql://%s:%s/%s";

    //TODO: Get this from a config file
    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DATABASE = "xe";
    private static final String USER = "tgbdev1_root";
    private static final String PASSWORD = "system";


    @Override
    public DataSource getDataSource() throws SQLException {
        String connectionURL = String.format(JDBC_URL, HOST, PORT, DATABASE);
        MysqlConnectionPoolDataSource source =  new MysqlConnectionPoolDataSource();
        source.setURL(connectionURL);
        source.setUser(USER);
        source.setPassword(PASSWORD);
        return source;
    }

}
