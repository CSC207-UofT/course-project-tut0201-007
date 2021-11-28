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
public class DistanceFilter implements Filter {

    /**
     * Constructor that assigns int for hours as the interval
     *
     */
    public DistanceFilter() {

    }
    /**
     * Essentially, we are returning false if the schedule does not pass the filter, otherwise
     * return the schedule, so we can layer
     *
     * @param s is a schedule we want to check follows the "interval" rule
     */
    @Override
    public boolean checkSchedule(Schedule s) {
        return true;
    }
}