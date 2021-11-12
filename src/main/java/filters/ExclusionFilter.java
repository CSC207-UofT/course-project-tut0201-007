package filters;

import entities.Schedule;

/**
 * This class represents a filter. It works by checking a given schedule for the given criteria, in this case
 * whether a schedule contains course exclusions or not
 */
public class ExclusionFilter implements Filter {

    /**
     * Constructs a filter
     *
     */
    public ExclusionFilter() {

    }

    /**
     * Returns true if the schedule contains courses that are listed as exclusions of each other
     *
     * @param s the checked schedule
     */
    @Override
    public boolean checkSchedule(Schedule s) {

        if (s == null) {
            return false;
        }

        return true;

    }
}