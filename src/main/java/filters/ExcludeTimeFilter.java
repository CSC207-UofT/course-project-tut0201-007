package filters;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class filter checks whether a course overlaps with a specific time. The schedule does not pass the filter if
 * it overlaps.
 */
public class ExcludeTimeFilter implements Filter {

    private LocalTime lowerBound;
    private LocalTime upperBound;
    private Day filteredDay;

    /**
     * {@code filteredDay} defaults to Day.ALL_DAYS, meaning restriction to time range holds for all
     * days.
     *
     * @see ExcludeTimeFilter(LocalTime, LocalTime, Day)
     */
    public ExcludeTimeFilter(LocalTime lowerBound, LocalTime upperBound) {
        this(lowerBound, upperBound, Day.ALL_DAYS);
    }

    /**
     * @param lowerBound inclusive Lower end of time range courses should be excluded from
     * @param upperBound Inclusive Upper bound of time range courses should be excluded from
     * @param filteredDay Day to which these bounds are applied (Or all of them)
     */
    public ExcludeTimeFilter(LocalTime lowerBound, LocalTime upperBound, Day filteredDay) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.filteredDay = filteredDay;
    }

    /**
     * @param s Schedule to check
     * @return Whether some section is inside the time range
     */
    @Override
    public boolean checkSchedule(Schedule s) {
        if (s == null) {
            return false;
        }

        // only need timeslots for the checking
        List<Timeslot> timeslots = new ArrayList();

        for (Section lec : s.getLectures()) {
            timeslots.addAll(lec.getTimes());
        }

        for (Section tut : s.getTutorials()) {
            timeslots.addAll(tut.getTimes());
        }

        for (Timeslot timeslot : timeslots) {
            if (overlapsBounds(timeslot, filteredDay)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param timeslot Timeslot to check if it conflicts with specified range
     * @param filteredDay Day that time restriction is applied to
     * @return Whether the timeslot is partially within the desired time range (inclusive)
     */
    private boolean overlapsBounds(Timeslot timeslot, Day filteredDay) {
        if (filteredDay == Day.ALL_DAYS) {
            return !(lowerBound.compareTo(timeslot.getEnd()) >= 0
                    || upperBound.compareTo(timeslot.getStart()) <= 0);
        } else {
            return filteredDay.getDay() == timeslot.getDay()
                    && !(lowerBound.compareTo(timeslot.getEnd()) >= 0
                    || upperBound.compareTo(timeslot.getStart()) <= 0);
        }
    }
}
