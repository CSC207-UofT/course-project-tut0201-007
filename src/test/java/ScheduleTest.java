import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ScheduleTest {
    Schedule schedule;
    ArrayList<Session> lecsToAdd;
    ArrayList<Session> tutsToAdd;
    Session lec1, lec2, lec3;
    Session tut1, tut2, tut3;
    LocalTime startTime1, startTime2, startTime3;
    LocalTime endTime1, endTime2, endTime3;
    DayOfWeek day;

    @Before
    public void setUp() {
        schedule = new Schedule();

        startTime1 = LocalTime.of(9, 0, 0);
        endTime1 = LocalTime.of(10, 0, 0);
        startTime2 = LocalTime.of(16, 0, 0);
        endTime2 = LocalTime.of(17, 0, 0);
        startTime3 = LocalTime.of(13, 0, 0);
        endTime3 = LocalTime.of(14, 0, 0);

        day = DayOfWeek.MONDAY;

        lec1 = new Session("LEC-0101", "SS 2135", startTime1, endTime1, day);
        lec2 = new Session("LEC-0201", "SS 2135", startTime2, endTime2, day);
        lec3 = new Session("LEC-0301", "SS 2135", startTime3, endTime3, day);

        tut1 = new Session("TUT-0101", "MP203", startTime1, endTime1, day);
        tut2 = new Session("TUT-0201", "MP203", startTime2, endTime2, day);
        tut3 = new Session("TUT-0301", "MP203", startTime3, endTime3, day);

        lecsToAdd = new ArrayList<>(List.of(lec1, lec2, lec3));
        tutsToAdd = new ArrayList<>(List.of(tut1, tut2, tut3));
    }

    @Test(timeout = 50)
    public void testAddLectures() {

        ArrayList<Session> expected = new ArrayList<>(List.of(lec1, lec3, lec2));

        for (Session lec: lecsToAdd) {
            schedule.addLecture(lec);
        }
        assertEquals(expected, schedule.getLectures());
    }

    @Test(timeout = 50)
    public void testAddLecturesSingleBefore() {

        schedule = new Schedule();
        schedule.addLecture(lec1);
        ArrayList<Session> expected = new ArrayList<>(List.of(lec1, lec2));

        schedule.addLecture(lec2);

        assertEquals(expected, schedule.getLectures());
    }

    @Test(timeout = 50)
    public void testAddLecturesSingleAfter() {

        schedule = new Schedule();
        schedule.addLecture(lec2);
        ArrayList<Session> expected = new ArrayList<>(List.of(lec1, lec2));

        schedule.addLecture(lec1);

        assertEquals(expected, schedule.getLectures());
    }

    @Test(timeout = 50)
    public void testAddTutorials() {

        ArrayList<Session> expected = new ArrayList<>(List.of(tut1, tut3, tut2));

        for (Session tut: tutsToAdd) {
            schedule.addTutorial(tut);
        }
        assertEquals(expected, schedule.getTutorials());

    }

}
