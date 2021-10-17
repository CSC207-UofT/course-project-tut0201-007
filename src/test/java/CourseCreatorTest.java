import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CourseCreatorTest {
    CourseCreator creator = new CourseCreator();
    Course course1;
    Course course2;
    Course course3;

    @Before
    public void setUp() throws Exception {
        course1 = creator.generateCourse("MAT237");
        course2 = creator.generateCourse("MAT157");
        course3 = creator.generateCourse("COG250");
    }

    @Test(timeout = 100)
    public void testGenerateCourse() {
        String expected = "MAT237Y1";
        // Checking that the course has the correct code
        assertEquals(expected, course1.getCourseId());
    }

    @Test(timeout = 100)
    public void testLectureSessions() {
        String expected = "LEC in MP 203 from 09:00 to 10:00 on MONDAY";
        // Checking that the course has the correct lectures
        assertEquals(expected, course1.getLectures().get(0).toString());
    }

    @Test(timeout = 100)
    public void testTutorialSessions() {
        String expected = "TUT in MY 380 from 10:00 to 11:00 on TUESDAY";
        // Checking that the course has the correct tutorials
        System.out.println(course1.getTutorials());
        assertEquals(expected, course1.getTutorials().get(0).toString());
    }

    @Test(timeout = 100)
    public void testLectureSessionsAsync() {
        String expected = "LEC in ONLINE from 00:00 to 00:00 on SATURDAY";
        // Checking that the course has no scheduled lectures
        assertEquals(expected, course3.getLectures().get(0).toString());
    }
}
