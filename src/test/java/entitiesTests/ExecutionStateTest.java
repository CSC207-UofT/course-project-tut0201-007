package entitiesTests;

import static org.junit.Assert.*;

import entities.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ExecutionStateTest {
    ExecutionState exe;
    Course course;

    @Before
    public void setUp() {

        LocalTime startTime1 = LocalTime.of(9, 0, 0);
        LocalTime endTime1 = LocalTime.of(10, 0, 0);
        LocalTime startTime2 = LocalTime.of(16, 0, 0);
        LocalTime endTime2 = LocalTime.of(17, 0, 0);
        LocalTime startTime3 = LocalTime.of(13, 0, 0);
        LocalTime endTime3 = LocalTime.of(14, 0, 0);

        String lectureLocation = "SS 2135";
        String tutorialLocation = "MP203";
        DayOfWeek day = DayOfWeek.MONDAY;

        Timeslot lec1 = new Timeslot(startTime1, endTime1, day, lectureLocation, 'F');
        Timeslot lec2 = new Timeslot(startTime2, endTime2, day, lectureLocation, 'F');
        Timeslot lec3 = new Timeslot(startTime3, endTime3, day, lectureLocation, 'F');

        Timeslot tut1 = new Timeslot(startTime1, endTime1, day, tutorialLocation, 'F');
        Timeslot tut2 = new Timeslot(startTime2, endTime2, day, tutorialLocation, 'F');
        Timeslot tut3 = new Timeslot(startTime3, endTime3, day, tutorialLocation, 'F');

        Section lec1Section = new Section("TST101 LEC0101 F", List.of(lec1));
        Section lec2Section = new Section("TST101 LEC0201 F", List.of(lec2));
        Section lec3Section = new Section("TST101 LEC0301 F", List.of(lec3));

        Section tut1Section = new Section("TST101 TUT0202 F", List.of(tut1));
        Section tut2Section = new Section("TST202 TUT0202 F", List.of(tut2));
        Section tut3Section = new Section("TST303 TUT0202 F", List.of(tut3));

        List<Section> lecsToAdd = List.of(lec1Section, lec2Section, lec3Section);
        List<Section> tutsToAdd = List.of(tut1Section, tut2Section, tut3Section);

        List<String> prereq = List.of("Intro to Testing 101");
        List<String> coreq = List.of("Beep Boop 101");

        course = new Course("Advanced Testing 200", lecsToAdd, tutsToAdd, 'Y', coreq, prereq);
        exe = new ExecutionState();
    }

    @Test(timeout = 100)
    public void testSetUserCourses() {
        List<Course> expected = List.of(course);
        exe.setUserCourses(expected);

        assert exe.getUserCourses().equals(expected);
    }

    @Test(timeout = 100)
    public void testRemainingUserCourses() {
        List<Course> expected = List.of();
        exe.setUserCourses(List.of(course));

        assert exe.getRemainingCourses().equals(expected);
    }

    @Test(timeout = 100)
    public void testCurrentCourse() {
        Course expected = course;
        exe.setUserCourses(List.of(course));

        assert exe.getCurrentCourse().equals(expected);
    }
}
