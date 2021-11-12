package filters;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.util.ArrayList;

/**
 * This class represents a filter. It works by checking a given schedule for the given criteria, in this case
 * whether a schedule is in-person or not.
 */
public class InPersonFilter implements Filter {
    private final boolean inPerson;

    /**
     * Constructs a filter with the desired delivery method
     *
     * @param inPerson the preference for in-person or online sections
     */
    public InPersonFilter(boolean inPerson) {
        this.inPerson = inPerson;
    }

    /**
     * Returns true if the schedule only contains timeslots with the given delivery method, otherwise false
     *
     * @param s the checked schedule
     */
    @Override
    public boolean checkSchedule(Schedule s) {
        if (s == null) {
            return false;
        }

        ArrayList<Timeslot> timeslots = new ArrayList<>();

        for (Section lec : s.getLectures()) {
            timeslots.addAll(lec.getTimes());
        }

        for (Section tut : s.getTutorials()) {
            timeslots.addAll(tut.getTimes());
        }

        for (Timeslot timeslot : timeslots) {
            boolean isOnline = timeslot.getRoom().equals("ONLINE");
            // if the desired teaching method is not satisfied by the timeslot
            if ((inPerson && isOnline) || (!inPerson && !isOnline)) {
                return false;
            }
        }

        return true;
    }
}
