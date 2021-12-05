package entitiesTests;

import static org.junit.Assert.*;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import entities.ASCIIFormatter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ASCIIFormatterTest {
    Schedule sched;
    LocalTime startTime1, endTime2;

    @Before
    public void setUp() {
        sched = new Schedule();
        Section lec1Section, lec2Section, lec3Section;
        Section tut1Section, tut2Section, tut3Section;
        List<Section> lecsToAdd, tutsToAdd;

        startTime1 = LocalTime.of(9, 0, 0);
        LocalTime endTime1 = LocalTime.of(10, 0, 0);
        LocalTime startTime2 = LocalTime.of(16, 0, 0);
        endTime2 = LocalTime.of(17, 0, 0);
        LocalTime startTime3 = LocalTime.of(13, 0, 0);
        LocalTime endTime3 = LocalTime.of(14, 0, 0);

        String lectureLocation = "SS 2135";
        String tutorialLocation = "MP203";
        DayOfWeek day = DayOfWeek.MONDAY;

        Timeslot lec1 = new Timeslot(startTime1, endTime1, day, lectureLocation);
        Timeslot lec2 = new Timeslot(startTime2, endTime2, day, lectureLocation);
        Timeslot lec3 = new Timeslot(startTime3, endTime3, day, lectureLocation);

        Timeslot tut1 = new Timeslot(startTime1, endTime1, day, tutorialLocation);
        Timeslot tut2 = new Timeslot(startTime2, endTime2, day, tutorialLocation);
        Timeslot tut3 = new Timeslot(startTime3, endTime3, day, tutorialLocation);

        lec1Section = new Section("TST101 LEC0101 F", List.of(lec1));
        lec2Section = new Section("TST101 LEC0201 F", List.of(lec2));
        lec3Section = new Section("TST101 LEC0301 F", List.of(lec3));

        tut1Section = new Section("TST101 TUT0202 F", List.of(tut1));
        tut2Section = new Section("TST202 TUT0202 F", List.of(tut2));
        tut3Section = new Section("TST303 TUT0202 F", List.of(tut3));

        lecsToAdd = List.of(lec1Section, lec2Section, lec3Section);
        tutsToAdd = List.of(tut1Section, tut2Section, tut3Section);

        for (Section lec : lecsToAdd) {
            sched.addLecture(lec);
        }

        for (Section tut : tutsToAdd) {
            sched.addLecture(tut);
        }
    }

    @Test(timeout = 50)
    public void testGetEarly() {
        ASCIIFormatter ascii = new ASCIIFormatter(sched);

        LocalTime expected = startTime1;
        assertEquals(expected, ascii.getStart());
    }

    @Test(timeout = 50)
    public void testGetLate() {
        ASCIIFormatter ascii = new ASCIIFormatter(sched);

        LocalTime expected = endTime2;
        assertEquals(expected, ascii.getEnd());
    }
}
