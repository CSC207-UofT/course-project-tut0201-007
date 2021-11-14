import entities.Timeslot;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

public class TimeslotTest {

    Timeslot timeslotA, timeslotB;
    Timeslot different;

    @Before
    public void setUp() {
        LocalTime now = LocalTime.now();
        LocalTime end = now.plusHours(1);
        DayOfWeek today = DayOfWeek.MONDAY;
        String location = "The Office";

        timeslotA = new Timeslot(now, end, today, location, 'F');
        timeslotB = new Timeslot(now, end, today, location, 'F');
        different = new Timeslot(now, now.plusHours(2), today, location, 'F');
    }

    @Test(timeout = 1000)
    public void testEquality() {
        assert (timeslotA.equals(timeslotB));
    }

    @Test(timeout = 100)
    public void testInequality() {
        assert (!timeslotA.equals(different));
    }
}
