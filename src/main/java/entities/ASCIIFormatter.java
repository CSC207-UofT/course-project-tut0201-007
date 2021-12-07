package entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class ASCIIFormatter {

    private final Schedule schedule;
    private final LocalTime start;
    private final LocalTime end;
    private final LocalTime latest;
    private final ArrayList<Timeslot> timeslots;

    /**
     * Constructor that takes schedule to be turned into ASCII output.
     *
     * @param sched is the schedule to be formatted
     */
    public ASCIIFormatter(Schedule sched) {
        this.schedule = sched;
        this.timeslots = populateTimeslots();
        this.start = getEarly();
        this.end = getLate();
        this.latest = getLatest();
    }

    /**
     * getter method for start
     *
     * @return earliest time in the schedule
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * getter method for end
     *
     * @return latest start time in the schedule
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * getter method for timeslots
     *
     * @returnlist of timeslots
     */
    public ArrayList<Timeslot> getTimeslots() {
        return timeslots;
    }

    /**
     * getter method for timeslots
     *
     * @return array list of all timeslots in the schedule (in no particular order)
     */
    public ArrayList<Timeslot> populateTimeslots() {
        ArrayList<Timeslot> timeslots = new ArrayList<>();

        for (Section lec : this.schedule.getLectures()) {
            timeslots.addAll(lec.getTimes());
        }

        for (Section tut : this.schedule.getTutorials()) {
            timeslots.addAll(tut.getTimes());
        }

        return timeslots;
    }

    /**
     * Method to turn our timeslots into a nx5 array that can be used to construct our ascii
     * schedule.
     *
     * <p>The array needs to have 5 columns, and variable height based on the hours slots we have.
     * Each element is a Timeslot, which will be used in genTable(). Note that the same timeslot
     * appears multiple times if the timeslot is longer than an hour.
     *
     * @return nx5 matrix of either empty strings or timeslots for wherever they are in the schedule (relatively)
     */
    public String[][] populateMatrix() {
        String[][] mat = new String[latest.getHour() - start.getHour()][5];

        // pre populate matrix
        for (String[] strings : mat) {
            Arrays.fill(strings, "");
        }

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                for (Timeslot timeslot : timeslots) {
                    if (j == (timeslot.getDay().getValue() - 1)
                            && i == (timeslot.getStart().getHour() - start.getHour())) {
                        // ie if 2 hour long lec then curr cell and cell below should get filled w/ same timeslot
                        for (int l = 0; l < timeslot.getEnd().getHour() - timeslot.getStart().getHour(); l++) {
                            mat[i+l][j] = timeslot.toString();
                        }
                    }
                }
            }
        }

        return mat;
    }

    /** Method to grab the earliest start time of a course in a schedule. */
    private LocalTime getEarly() {
        LocalTime min = LocalTime.parse("23:00:00");
        for (Timeslot timeslot : timeslots) {
            if (timeslot.getStart().compareTo(min) < 0) {
                min = timeslot.getStart();
            }
        }
        return min;
    }

    /** Method to grab the latest START time of a course in a schedule. */
    private LocalTime getLate() {
        LocalTime max = LocalTime.parse("00:00:00");
        for (Timeslot timeslot : timeslots) {
            if (timeslot.getStart().compareTo(max) > 0) {
                max = timeslot.getStart();
            }
        }
        return max;
    }

    /** Method to grab the latest END time of a course in a schedule. */
    private LocalTime getLatest() {
        LocalTime max = LocalTime.parse("00:00:00");
        for (Timeslot timeslot : timeslots) {
            if (timeslot.getEnd().compareTo(max) > 0) {
                max = timeslot.getEnd();
            }
        }
        return max;
    }

    public String genTable() {
        String leftAlignFormat = "| %-24s | %-24s | %-24s | %-24s | %-24s |\n";
        String padding =
                "|                          |                          |                          |                          |                  "
                        + "        |\n";
        String floor =
                "+--------------------------+--------------------------+--------------------------+--------------------------+--------------------------+\n";
        StringBuilder output = new StringBuilder("Schedule: \n\n");
        String[][] arr = populateMatrix();

        // The header for our schedule
        output.append(floor)
                .append(
                        "|          Monday          |          Tuesday         |         Wednesday        |         Thursday         |        "
                                + "  Friday          |\n")
                .append(floor);
        for (int i = 0; i < latest.getHour() - start.getHour(); i++) {
            output.append(padding);
            output.append(String.format(leftAlignFormat, arr[i][0], arr[i][1], arr[i][2], arr[i][3], arr[i][4]));
            output.append(padding);
            output.append(floor);
        }

        return output.toString();
    }
}
