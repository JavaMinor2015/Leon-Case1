package nl.stoux.minor.mocking;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Leon Stam on 7-10-2015.
 */
public abstract class MockPrepStatement implements PreparedStatement {

    @Override public void setInt(int parameterIndex, int x) throws SQLException {}
    @Override public void setString(int parameterIndex, String x) throws SQLException {}
    @Override public void setDate(int parameterIndex, Date x) throws SQLException {}

}
