package nl.stoux.minor.data;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by Leon Stam on 5-10-2015.
 */
public interface DataSourceProvider {

    /**
     * Get a DataSource
     * @return the source
     * @throws SQLException
     */
    DataSource getDataSource() throws SQLException;

}
