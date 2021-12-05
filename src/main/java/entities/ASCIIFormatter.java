package entities;

import java.time.LocalTime;
import java.util.ArrayList;

public class ASCIIFormatter {

    private final Schedule schedule;
    private final LocalTime start;
    private final LocalTime end;

    /**
     * Constructor that takes schedule to be turned into ASCII output.
     *
     * @param sched is the schedule to be formatted
     */
    public ASCIIFormatter(Schedule sched) {
        this.schedule = sched;
        this.start = getEarly(sched);
        this.end = getEnd(sched);
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    /**
     * Method to grab the earliest start time of a course in a schedule.
     *
     * @param s is the schedule we want the earliest time of.
     */
    private LocalTime getEarly(Schedule s) {
        ArrayList<Timeslot> timeslots = s.getTimeslots();
        LocalTime min = LocalTime.parse("23:00:00");
        for (Timeslot timeslot: timeslots){
            if(timeslot.getStart().compareTo(min) < 0) {
                min = timeslot.getStart();
            }
        }
        return min;
    }

    /**
     * Method to grab the latest start time of a course in a schedule.
     *
     * @param s is the schedule we want the earliest time of.
     */
    private LocalTime getEnd(Schedule s) {
        ArrayList<Timeslot> timeslots = s.getTimeslots();
        LocalTime max = LocalTime.parse("00:00:00");
        for (Timeslot timeslot: timeslots){
            if(timeslot.getEnd().compareTo(max) > 0) {
                max = timeslot.getEnd();
            }
        }
        return max;
    }

    public String genTable() {
        String leftAlignFormat = "| %-15s | %-4d |\n";
        StringBuilder output = new StringBuilder("Schedule: \n\n");

        // The header for our schedule
        output.append("+----------------+----------------+----------------+----------------+----------------+\n" +
                "|     Monday     |     Tuesday    |    Wednesday   |    Thursday    |     Friday     |\n" +
                "+----------------+----------------+----------------+----------------+----------------+\n");
        for (int i = start.getHour(); i < end.getHour(); i++) {
            output.append(String.format(leftAlignFormat, "PogU", i * i));
        }
        output.append("+----------------+----------------+----------------+----------------+----------------+\n");

        return output.toString();
    }
}
