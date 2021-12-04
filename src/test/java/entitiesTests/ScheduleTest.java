package entitiesTests;

import static org.junit.Assert.*;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ScheduleTest {
    Section lec1Section, lec2Section, lec3Section;
    Section tut1Section, tut2Section, tut3Section;
    List<Section> lecsToAdd, tutsToAdd;

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

        lec1Section = new Section("TST101 LEC0101 F", List.of(lec1));
        lec2Section = new Section("TST101 LEC0201 F", List.of(lec2));
        lec3Section = new Section("TST101 LEC0301 F", List.of(lec3));

        tut1Section = new Section("TST101 TUT0202 F", List.of(tut1));
        tut2Section = new Section("TST202 TUT0202 F", List.of(tut2));
        tut3Section = new Section("TST303 TUT0202 F", List.of(tut3));

        lecsToAdd = List.of(lec1Section, lec2Section, lec3Section);
        tutsToAdd = List.of(tut1Section, tut2Section, tut3Section);
    }

    @Test(timeout = 50)
    public void testAddLectures() {
        Schedule schedule = new Schedule();

        List<Section> expected = List.of(lec1Section, lec2Section, lec3Section);

        for (Section lec : lecsToAdd) {
            schedule.addLecture(lec);
        }
        assertEquals(expected, schedule.getLectures());
    }

    @Test(timeout = 50)
    public void testAddLecturesSingleBefore() {

        Schedule schedule = new Schedule();
        schedule.addLecture(lec1Section);

        List<Section> expected = List.of(lec1Section, lec2Section);

        schedule.addLecture(lec2Section);
        assertEquals(expected, schedule.getLectures());
    }

    @Test(timeout = 50)
    public void testAddLecturesSingleAfter() {

        Schedule schedule = new Schedule();
        schedule.addLecture(lec1Section);
        List<Section> expected = List.of(lec1Section, lec2Section);
        schedule.addLecture(lec2Section);

        assertEquals(expected, schedule.getLectures());
    }

    @Test(timeout = 50)
    public void testAddTutorials() {
        Schedule schedule = new Schedule();

        List<Section> expected = List.of(tut1Section, tut2Section, tut3Section);

        for (Section tut : tutsToAdd) {
            schedule.addTutorial(tut);
        }
        assertEquals(expected, schedule.getTutorials());
    }
}
