import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

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

        lec1 =
                new Session.Builder("LEC-0101")
                        .inRoom("SS 2135")
                        .startsAt(startTime1)
                        .endsAt(endTime1)
                        .onDay(day)
                        .build();
        lec2 =
                new Session.Builder("LEC-0201")
                        .inRoom("SS 2135")
                        .startsAt(startTime2)
                        .endsAt(endTime2)
                        .onDay(day)
                        .build();
        lec3 =
                new Session.Builder("LEC-0301")
                        .inRoom("SS 2135")
                        .startsAt(startTime3)
                        .endsAt(endTime3)
                        .onDay(day)
                        .build();

        tut1 =
                new Session.Builder("TUT-0101")
                        .inRoom("MP203")
                        .startsAt(startTime1)
                        .endsAt(endTime1)
                        .onDay(day)
                        .build();
        tut2 =
                new Session.Builder("TUT-0201")
                        .inRoom("MP203")
                        .startsAt(startTime2)
                        .endsAt(endTime2)
                        .onDay(day)
                        .build();
        tut3 =
                new Session.Builder("TUT-0301")
                        .inRoom("MP203")
                        .startsAt(startTime3)
                        .endsAt(endTime3)
                        .onDay(day)
                        .build();

        lecsToAdd = new ArrayList<>(List.of(lec1, lec2, lec3));
        tutsToAdd = new ArrayList<>(List.of(tut1, tut2, tut3));
    }

    @Test(timeout = 50)
    public void testAddLectures() {

        ArrayList<Session> expected = new ArrayList<>(List.of(lec1, lec3, lec2));

        for (Session lec : lecsToAdd) {
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

        for (Session tut : tutsToAdd) {
            schedule.addTutorial(tut);
        }
        assertEquals(expected, schedule.getTutorials());
    }
}
