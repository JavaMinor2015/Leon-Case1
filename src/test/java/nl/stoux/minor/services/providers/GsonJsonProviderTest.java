package nl.stoux.minor.services.providers;

import nl.stoux.minor.TestMixin;
import nl.stoux.minor.domain.Course;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Created by Stoux on 05/10/2015.
 */
public class GsonJsonProviderTest implements TestMixin {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private GsonJsonProvider provider;
    private InputStream jsonFile;

    @Before
    public void setup() {
        provider = new GsonJsonProvider();
        jsonFile = getTestResource("GsonProvider/course.json");
    }


    @Test
    public void testWriteTo() throws Exception {
        //Arrange
        Course course = new Course("A", "B");
        File tempFile = folder.newFile();
        FileOutputStream outputStream = new FileOutputStream(tempFile);

        //Act
        provider.writeTo(course, Course.class, Course.class, null, null, null, outputStream);
        outputStream.flush();
        outputStream.close();

        //Assert
        assertTrue(matchInputs(jsonFile, new FileInputStream(tempFile)));
    }

    /**
     * Try to match two inputs stream
     * @param stream1 The first stream
     * @param stream2 The second stream
     * @return matches
     * @throws Exception
     */
    private boolean matchInputs(InputStream stream1, InputStream stream2) throws Exception {
        try (
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(stream1));
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(stream2))
        ) {

            String line;
            while ((line = reader1.readLine()) != null) {
                String otherLine = reader2.readLine();
                if (!(line.equals(otherLine))) {
                    return false;
                }
            }

            //Check if other reader is empty aswell
            return reader2.readLine() == null;
        }
    }




    @Test
    public void testReadFrom() throws Exception {
        //Act
        Object courseObject = provider.readFrom(null, Course.class, null, null, null, jsonFile);

        //Assert
        assertNotNull(courseObject);
        assertTrue(courseObject instanceof Course);

        Course course = (Course) courseObject;
        assertEquals("A", course.getCode());
        assertEquals("B", course.getTitle());
    }

}