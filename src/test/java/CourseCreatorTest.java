import static org.junit.Assert.*;

import java.io.IOException;
import entities.Course;
import org.junit.Before;
import org.junit.Test;
import workers.CourseCreator;

public class CourseCreatorTest {
    Course course1;
    Course course2;
    Course course3;

    @Before
    public void setUp() {
        try {
            course1 = CourseCreator.generateCourse("MAT237", 'Y');
            course2 = CourseCreator.generateCourse("MAT157", 'Y');
            course3 = CourseCreator.generateCourse("COG250", 'Y');
        } catch (IOException e) {
            System.out.println("there is an issue");
        }
    }

    @Test(timeout = 100)
    public void testGenerateCourse() {
        String expected = "MAT237";
        // Checking that the course has the correct code
        assertEquals(expected, course1.getCourseId());
    }

    @Test(timeout = 100)
    public void testLectureSections() {
        String expected =
                "LEC-0101 meets at:\n"
                        + "MONDAY from 09:00-10:00 at MP 203\n"
                        + "TUESDAY from 09:00-10:00 at MP 103\n"
                        + "THURSDAY from 09:00-10:00 at MP 203\n";
        // Checking that the course has the correct lectures
        assertEquals(expected, course1.getLectures().get(0).toString());
    }

    @Test(timeout = 100)
    public void testTutorialSections() {
        String expected = "TUT-0101 meets at:\nTUESDAY from 10:00-11:00 at MY 380\n";
        // Checking that the course has the correct tutorials
        assertEquals(expected, course1.getTutorials().get(0).toString());
    }

    @Test(timeout = 100)
    public void testLectureSectionsAsync() {
        String expected = "LEC-0101 is ASYNC";
        // Checking that the course has no scheduled lectures
        assertEquals(expected, course3.getLectures().get(0).toString());
    }
}
