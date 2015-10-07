package nl.stoux.minor;

import nl.stoux.minor.data.DataSourceProvider;
import nl.stoux.minor.data.MockDataSourceProvider;

/**
 * Created by Leon Stam on 7-10-2015.
 */
public class TestMain extends Main {

    @Override
    protected DataSourceProvider getProvider() {
        return new MockDataSourceProvider();
    }
}
