package entitiesTests;

import static org.junit.Assert.*;

import entities.ASCIIFormatter;
import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class ASCIIFormatterTest {
    Schedule sched;
    LocalTime startTime1, startTime2, endTime2;
    Timeslot lec1, lec2, lec3, tut1, tut2, tut3;
    Section lec1Section, lec2Section, lec3Section;
    Section tut1Section, tut2Section, tut3Section;

    @Before
    public void setUp() {
        sched = new Schedule();
        List<Section> lecsToAdd, tutsToAdd;

        startTime1 = LocalTime.of(9, 0, 0);
        LocalTime endTime1 = LocalTime.of(10, 0, 0);
        startTime2 = LocalTime.of(15, 0, 0);
        endTime2 = LocalTime.of(17, 0, 0);
        LocalTime startTime3 = LocalTime.of(13, 0, 0);
        LocalTime endTime3 = LocalTime.of(14, 0, 0);

        String lectureLocation = "SS 2135";
        String tutorialLocation = "MP203";
        DayOfWeek day0 = DayOfWeek.MONDAY;
        DayOfWeek day2 = DayOfWeek.WEDNESDAY;

        lec1 = new Timeslot(startTime1, endTime1, day0, lectureLocation, 'F');
        lec2 = new Timeslot(startTime2, endTime2, day2, lectureLocation, 'F');
        lec3 = new Timeslot(startTime3, endTime3, day0, lectureLocation, 'F');

        tut1 = new Timeslot(startTime1, endTime1, day2, tutorialLocation, 'F');
        tut2 = new Timeslot(startTime2, endTime2, day0, tutorialLocation, 'F');
        tut3 = new Timeslot(startTime3, endTime3, day2, tutorialLocation, 'F');

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
            sched.addTutorial(tut);
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

        LocalTime expected = startTime2;
        assertEquals(expected, ascii.getEnd());
    }

    @Test(timeout = 50)
    public void testGetLatest() {
        ASCIIFormatter ascii = new ASCIIFormatter(sched);

        LocalTime expected = endTime2;
        assertEquals(expected, ascii.getEndLate());
    }

    @Test(timeout = 50)
    public void testGetTimeslots() {
        ASCIIFormatter ascii = new ASCIIFormatter(sched);

        Set<Timeslot> expected = new HashSet<>();
        expected.add(lec1);
        expected.add(lec2);
        expected.add(lec3);
        expected.add(tut1);
        expected.add(tut2);
        expected.add(tut3);

        assert expected.equals(ascii.getTimeslots());
    }

    @Test(timeout = 100)
    public void testPopulateMatrixShape() {
        ASCIIFormatter ascii = new ASCIIFormatter(sched);
        String[][] expected = new String[8][5];

        assertEquals(expected.length, ascii.populateMatrix('F').length);
        assertEquals(expected[0].length, ascii.populateMatrix('F')[0].length);
    }

    @Test(timeout = 200)
    public void testPopulateMatrixContent() {
        ASCIIFormatter ascii = new ASCIIFormatter(sched);
        String[][] expected = new String[8][5];
        String[][] matrix = ascii.populateMatrix('F');

        for (String[] strings : expected) {
            Arrays.fill(strings, "");
        }

        expected[0][0] = lec1Section.getName().split(" ")[0] + " " + lec1.quickString();
        expected[0][2] = tut1Section.getName().split(" ")[0] + " " + tut1.quickString();
        expected[6][2] = lec2Section.getName().split(" ")[0] + " " + lec2.quickString();
        expected[7][2] = lec2Section.getName().split(" ")[0] + " " + lec2.quickString();
        expected[6][0] = tut2Section.getName().split(" ")[0] + " " + tut2.quickString();
        expected[7][0] = tut2Section.getName().split(" ")[0] + " " + tut2.quickString();
        expected[4][0] = lec3Section.getName().split(" ")[0] + " " + lec3.quickString();
        expected[4][2] = tut3Section.getName().split(" ")[0] + " " + tut3.quickString();

        assertArrayEquals(expected, matrix);
    }
}
