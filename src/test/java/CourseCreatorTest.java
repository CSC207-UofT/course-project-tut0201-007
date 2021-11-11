import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CourseCreatorTest {
    CourseCreator creator = new CourseCreator();
    Course mockCourse;

    @Before
    public void setUp() throws Exception {
        mockCourse = creator.generateCourse("test");
    }

    @Test(timeout = 1000)
    public void testGenerateMockCourse() {
        String expected = "TST100";
        // Checking that the mock course has the correct code
        assertEquals(expected, mockCourse.getCourseId());
    }

    @Test(timeout = 1000)
    public void testMockLectureSessions() {
        String expected = "LEC in ROOM 01 from 09:00 to 10:00 on MONDAY";
        // Checking that the course has the correct lecture
        assertEquals(expected, mockCourse.getLectures().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testMockTutorialSessions() {
        String expected = "TUT in ROOM 05 from 10:00 to 11:00 on TUESDAY";
        // Checking that the course has the correct tutorial
        assertEquals(expected, mockCourse.getTutorials().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testMockTutorialSessionsSync() {
        String expected = "TUT in ONLINE from 10:00 to 11:00 on TUESDAY";
        // Checking that the course has the correct online tutorial
        assertEquals(expected, mockCourse.getTutorials().get(1).toString());
    }

    @Test(timeout = 1000)
    public void testMockTutorialSessionsAsync() {
        // Checking that async tutorials do not get added to the schedule
        assertEquals(2, mockCourse.getTutorials().size());
    }
}
