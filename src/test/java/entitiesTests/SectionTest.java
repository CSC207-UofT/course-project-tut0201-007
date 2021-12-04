package entitiesTests;

import entities.Section;
import entities.Timeslot;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class SectionTest {

    Timeslot timeslotA, timeslotB;
    Section sectionA, sectionB, different;

    @Before
    public void setUp() {
        LocalTime now = LocalTime.now();
        LocalTime end = now.plusHours(1);
        DayOfWeek today = DayOfWeek.MONDAY;
        String location = "The Office";

        timeslotA = new Timeslot(now, end, today, location, 'F');
        timeslotB = new Timeslot(now, end, today, location, 'F');

        List<Timeslot> timeslots = new ArrayList<>();
        ArrayList<Timeslot> differentTimeslots = new ArrayList<>();

        timeslots.add(timeslotA);
        timeslots.add(timeslotB);

        sectionA = new Section("TST101 LEC0101 F", timeslots);
        sectionB = new Section("TST101 LEC0101 F", new ArrayList(timeslots));

        different = new Section("TST101 LEC0101 F", differentTimeslots);
    }

    @Test(timeout = 1000)
    public void testEquality() {
        assert (sectionA.equals(sectionB));
    }

    @Test(timeout = 100)
    public void testInequality() {
        assert (!sectionA.equals(different));
    }
}
