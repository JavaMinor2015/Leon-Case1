package nl.stoux.minor.services.providers;

import nl.stoux.minor.domain.Course;
import nl.stoux.minor.domain.CourseInstance;
import nl.stoux.minor.domain.output.HateoasSupport;

import java.util.HashMap;

/**
 * Created by Stoux on 06/10/2015.
 */
public class UrlProvider {

    private static HashMap<Class<? extends HateoasSupport>, String> urlMap;
    static {
        urlMap = new HashMap<>();
        //urlMap.put(Company.class, Company.URL);
        urlMap.put(Course.class, Course.URL);
        //urlMap.put(CourseEnrolment.class, CourseEnrolment.URL);
        urlMap.put(CourseInstance.class, Course.URL);
        //urlMap.put(Student.class, Student.URL);
    }

    public static boolean hasUrl(Class<? extends HateoasSupport> clazz) {
        return urlMap.containsKey(clazz);
    }

    public static String getUrl(Class<? extends HateoasSupport> clazz) {
        return urlMap.get(clazz);
    }


}
