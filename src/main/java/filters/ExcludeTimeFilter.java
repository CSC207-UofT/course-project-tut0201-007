package filters;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a filter. It works by checking a given schedule for the given criteria. It
 * is made "stackable" by returning the schedule if it works, and then that can be used for further
 * calls.
 *
 * This filter excludes schedules with classes running in specified blocks of time.
 */
public class ExcludeTimeFilter implements Filter {
    private final List<Timeslot> timeExclusions;

    /**
     * Constructor that takes Timeslots during which there should be no class in a schedule.
     *
     * @param exclusions the time slots during which there should be no classes in a Schedule
     */
    public ExcludeTimeFilter(List<Timeslot> exclusions) {
        this.timeExclusions = exclusions;
    }
    /**
     * Essentially, we are returning nothing if the schedule does not pass the filter, otherwise
     * return the schedule, so we can layer
     *
     * @param s is a schedule we want to check if it has classes during the timeslots we want to exclude.
     */
    @Override
    public boolean checkSchedule(Schedule s) {
        if (s == null) {
            return false;
        }

        // only need timeslots for the checking
        ArrayList<Timeslot> timeslots = new ArrayList();

        for (Section lec : s.getLectures()) {
            timeslots.addAll(lec.getTimes());
        }

        for (Section tut : s.getTutorials()) {
            timeslots.addAll(tut.getTimes());
        }

        for (Timeslot exclude : timeExclusions) {
            for (Timeslot t : timeslots) {
                if (exclude.conflictsWith(t)) {
                    return false;
                }
            }
        }
        return true;
    }
}
