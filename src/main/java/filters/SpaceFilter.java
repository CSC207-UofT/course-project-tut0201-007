package filters;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.util.ArrayList;

/**
 * This class represents a filter. It works by checking a given schedule for the given criteria. It
 * is made "stackable" by returning the schedule if it works, and then that can be used for further
 * calls.
 */
public class SpaceFilter implements Filter {
    private final int interval;

    /**
     * Constructor that assigns int for hours as the interval
     *
     * @param hours is the hour(s) of "buffer" you would like between classes
     */
    public SpaceFilter(int hours) {
        this.interval = hours;
    }
    /**
     * Essentially, we are returning nothing if the schedule does not pass the filter, otherwise
     * return the schedule, so we can layer
     *
     * @param s is a schedule we want to check follows the "interval" rule
     */
    @Override
    public boolean checkSchedule(Schedule s) {
        // quick null type check
        if (s == null) {
            return false;
        }

        // only need timeslots for the checking
        ArrayList<Timeslot> timeslots = new ArrayList<>();

        for (Section lec : s.getLectures()) {
            timeslots.addAll(lec.getTimes());
        }

        for (Section tut : s.getTutorials()) {
            timeslots.addAll(tut.getTimes());
        }

        // check each timeslot for worst case O(n^2), but early null means this is optimal?
        for (int i = 0; i < timeslots.size(); i++) {
            for (int j = i + 1; j < timeslots.size(); j++) {
                if (timeslots.get(i).getDay() == timeslots.get(j).getDay()
                        && timeslots.get(i).getMaxDistance(timeslots.get(j)) < this.interval) {
                    return false;
                }
            }
        }

        return true;
    }
}
