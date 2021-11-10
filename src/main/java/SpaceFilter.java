import java.util.ArrayList;

/**
 * This class represents a filter. It works by checking a given schedule for the given criteria.
 * It is made "stackable" by returning the schedule if it works, and then that can be used for further calls.
 */
public class SpaceFilter implements Filter {
    // This is the interval between classes that you want, in hours
    private final int interval;

    public SpaceFilter(int hours) {
        this.interval = hours;
    }

    // Essentially, we are returning nothing if the schedule does not pass the filter,
    // otherwise return the schedule so we can layer
    @Override
    public Schedule checkSchedule(Schedule s) {
        // quick null type check
        if (s == null) {
            return null;
        }

        ArrayList<Timeslot> timeslots = new ArrayList();

        timeslots.addAll(s.getLectures());
        timeslots.addAll(s.getTutorials());

        for (int i = 0; i < timeslots.size(); i++) {
            for (int j = i+1; j < timeslots.size(); j++) {
                if (timeslots.get(i).end - timeslots.get(j).start < this.interval) {
                    return null;
                }
            }
        }

        return s;
    }
}
