package nl.stoux.minor;

import nl.stoux.minor.data.ConnectionManager;
import nl.stoux.minor.data.DataSourceProvider;
import nl.stoux.minor.data.sources.OracleDataSourceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Leon Stam on 5-10-2015.
 */
public class Main implements ServletContextListener {

    private Logger logger = LogManager.getLogger(Main.class.getSimpleName());

    private static ConnectionManager manager;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if (manager == null) {
            manager = setupConnectionManager();
        }
    }

    private ConnectionManager setupConnectionManager() {
        try {
            return new ConnectionManager(getProvider());
        } catch (SQLException e) {
            logger.fatal(e);
            //TODO: Stop the servlet, somehow?
            System.exit(1);
            return null;
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    /**
     * Get a Database connection
     * @return the connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return manager.getConnection();
    }

    protected DataSourceProvider getProvider() {
        return new OracleDataSourceProvider();
    }

}
