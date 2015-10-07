package nl.stoux.minor.mocking;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leon Stam on 7-10-2015.
 */
public abstract class MockResultSet implements ResultSet {

    private int index;

    private List<Map<String, Object>> entries;
    private int rows;

    public void setEntries(List<Map<String, Object>> entries) {
        this.entries = entries;
        rows = entries.size();
        index = -1;
    }

    @Override
    public void beforeFirst() throws SQLException {
        index = -1;
    }

    @Override
    public boolean next() throws SQLException {
        return (++index < rows);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return get(columnLabel);
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return get(columnLabel);
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return get(columnLabel);
    }

    private <X> X get(String label) {
        return (X) entries.get(index).get(label);
    }

    @Getter
    @AllArgsConstructor
    public static class RsEntry {
        private String key;
        private Object object;
    }

}
