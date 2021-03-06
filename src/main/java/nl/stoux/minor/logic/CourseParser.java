package nl.stoux.minor.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.stoux.minor.domain.Course;
import nl.stoux.minor.domain.CourseInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Leon Stam on 5-10-2015.
 */
public class CourseParser {

    private InputStream input;
    private HashMap<String, Course> courses;
    private DateTimeFormatter dateFormatter;

    private int currentLine = 0;

    @Getter
    private ParseResult result;

    public CourseParser(InputStream input, Collection<Course> knownCourses) throws ParseException {
        this.input = input;
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        courses = new HashMap<>();
        knownCourses.forEach(c -> courses.put(c.getCode(), c));

        result = parseInput();
    }

    /**
     * Parse the InputStream.
     * @return the ParseResult
     * @throws ParseException if an error occurred
     */
    private ParseResult parseInput() throws ParseException {
        Set<Course> newCourses = new HashSet<>();
        Set<CourseInstance> newInstances = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            //Get all blocks
            Optional<String[]> block;
            while((block = readBlock(reader)).isPresent()) {
                //Parse the block
                BlockResult blockResult = parseBlock(block.get());
                if (blockResult.getNewCourse().isPresent()) {
                    newCourses.add(blockResult.getNewCourse().get());
                }
                newInstances.add(blockResult.getNewInstance());

                //Read the spacer
                String line = readLine(reader);
                if (line == null) {
                    //End of file
                    break;
                }
            }
        } catch (IOException e) {
            throw new ParseException("Failed to read input: " + e.getMessage(), 0);
        }

        return new ParseResult(newCourses, newInstances);
    }

    private Optional<String[]> readBlock(BufferedReader reader) throws IOException, ParseException {
        String[] block = new String[4];
        for (int i = 0; i < 4; i++) {
            String line = readLine(reader);
            if (line == null) {
                if (i == 0) {
                    return Optional.empty();
                } else {
                    throw new ParseException("Unexpected end of file", currentLine);
                }
            }
            block[i] = line;
        }
        return Optional.of(block);
    }

    private BlockResult parseBlock(String[] block) throws ParseException {
        //Course info
        String title = validateAndReplace(block, 0, "Titel: ");
        String code = validateAndReplace(block, 1, "Cursuscode: ");

        //=> Find it
        Course course;
        Optional<Course> newCourse;
        if (courses.containsKey(code)) {
            course = courses.get(code);
            newCourse = Optional.empty();
        } else {
            course = new Course(code, title);
            courses.put(code, course);
            newCourse = Optional.of(course);
        }

        //Instance info
        block[2] = block[2].replace(" dagen", "");
        int duration = Integer.parseInt(validateAndReplace(block, 2, "Duur: "));
        LocalDate date = LocalDate.parse(validateAndReplace(block, 3, "Startdatum: "), dateFormatter);
        CourseInstance instance = new CourseInstance(course, duration, date);

        return new BlockResult(newCourse, instance);
    }

    private String validateAndReplace(String[] block, int index, String findReplace) throws ParseException {
        String line = block[index];
        if (!line.contains(findReplace)) {
            throw new ParseException("Invalid line: '" + findReplace.replace(": ", "") + "'", currentLine - (3 - index));
        }
        return block[index].replace(findReplace, "");
    }

    @Getter @AllArgsConstructor
    private class BlockResult {
        Optional<Course> newCourse;
        CourseInstance newInstance;
    }

    @Getter @AllArgsConstructor
    public class ParseResult {
        Collection<Course> newCourses;
        Collection<CourseInstance> newInstances;
    }

    private String readLine(BufferedReader reader) throws IOException {
        currentLine++;
        return reader.readLine();
    }

}
