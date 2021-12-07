package entitiesTests;

import entities.Timeslot;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TimeslotTest {

    Timeslot timeslotA, timeslotB;
    Timeslot different;
    Timeslot laterStart, latestStart;

    @Before
    public void setUp() {
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = start.plusHours(6);
        DayOfWeek today = DayOfWeek.MONDAY;
        String location = "The Office";

        timeslotA = new Timeslot(start, end, today, location, 'F');
        timeslotB = new Timeslot(start, end, today, location, 'F');
        different = new Timeslot(start, start.plusHours(2), today, location, 'F');

        LocalTime laterStartTime = start.plusHours(3);
        LocalTime latestStartTime = start.plusHours(4);

        laterStart = new Timeslot(laterStartTime, end, today, location, 'F');
        latestStart = new Timeslot(latestStartTime, end, today, location, 'F');
    }

    @Test(timeout = 1000)
    public void testEquality() {
        assert (timeslotA.equals(timeslotB));
    }

    @Test(timeout = 100)
    public void testInequality() {
        assert (!timeslotA.equals(different));
    }

    @Test(timeout = 100)
    public void testSorting() {
        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(latestStart);
        timeslots.add(timeslotA);
        timeslots.add(timeslotB);
        timeslots.add(laterStart);

        Timeslot[] preSort = timeslots.toArray(new Timeslot[timeslots.size()]);
        Collections.sort(timeslots);
        Timeslot[] postSort = timeslots.toArray(new Timeslot[timeslots.size()]);

        Timeslot[] expected = {timeslotA, timeslotB, laterStart, latestStart};
        assert (Arrays.equals(postSort, expected));
    }

    @Test(timeout = 100)
    public void testGetEnd() {
        assert timeslotA.getEnd().getHour() == 6;
    }
}
