package filters;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a filter. It works by checking a given schedule for the given criteria. It
 * is made "stackable" by returning the schedule if it works, and then that can be used for further
 * calls.
 *
 * This filter excludes schedules with classes running in specified blocks of time.
 */
public class IncludeTimeFilter implements Filter {
    private final List<Timeslot> timeInclusions;

    /**
     * Constructor that takes Timeslots during which there should be no class in a schedule.
     *
     * @param inclusions the time slots during which classes should take place
     */
    public IncludeTimeFilter(List<Timeslot> inclusions) {
        this.timeInclusions = inclusions;
    }
    /**
     * Returns schedules with all classes occuring during the specified times
     *
     * @param s is a schedule we want to check if all classes occur during the specified timeslots
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

        for (Timeslot include : timeInclusions) {
            DayOfWeek inclusionDay = include.getDay();
            for (Timeslot t : timeslots) {
                if (t.getDay().equals(inclusionDay) || !include.conflictsWith(t)) {
                    return false;
                }
            }
        }
        return true;
    }

}
