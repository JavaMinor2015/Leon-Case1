package nl.stoux.minor;

import java.io.InputStream;

/**
 * Created by Stoux on 05/10/2015.
 *
 * Let's try something new by using Mix-in's... Sorta.
 */
public interface TestMixin {

    default InputStream getTestResource(String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }

}
