package entities;

import java.time.LocalTime;
import java.util.ArrayList;

public class ASCIIFormatter {

    private final Schedule schedule;
    private final LocalTime start;
    private final LocalTime end;
    private final LocalTime latest;

    /**
     * Constructor that takes schedule to be turned into ASCII output.
     *
     * @param sched is the schedule to be formatted
     */
    public ASCIIFormatter(Schedule sched) {
        this.schedule = sched;
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
     * @return array list of all timeslots in the schedule (in no particular order)
     */
    public ArrayList<Timeslot> getTimeslots() {
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
        ArrayList<Timeslot> timeslots = getTimeslots();
        String[][] mat = new String[latest.getHour() - start.getHour()][5];

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                for (Timeslot timeslot : timeslots) {
                    if (j == (timeslot.getDay().getValue() - 1)
                            && i == (timeslot.getStart().getHour() - start.getHour())) {
//                        for (int k = 0; k < timeslot.getStart().getHour() - timeslot.getEnd().getHour(); k++) {
//                            mat[i][j + k] = timeslot.toString();
//                        }
                        mat[i][j] = timeslot.toString();
                    } else {
                        mat[i][j] = "n";
                    }
                }
            }
        }

        return mat;
    }

    public static String[] getColumn(String[][] arr, int index) {
        String[] col = new String[arr[0].length];
        for (int i = 0; i < col.length; i++) {
            col[i] = arr[i][index];
        }
        return col;
    }

    /** Method to grab the earliest start time of a course in a schedule. */
    private LocalTime getEarly() {
        ArrayList<Timeslot> timeslots = getTimeslots();
        LocalTime min = LocalTime.parse("23:00:00");
        for (Timeslot timeslot : timeslots) {
            if (timeslot.getStart().compareTo(min) < 0) {
                min = timeslot.getStart();
            }
        }
        return min;
    }

    /** Method to grab the latest start time of a course in a schedule. */
    private LocalTime getLate() {
        ArrayList<Timeslot> timeslots = getTimeslots();
        LocalTime max = LocalTime.parse("00:00:00");
        for (Timeslot timeslot : timeslots) {
            if (timeslot.getStart().compareTo(max) > 0) {
                max = timeslot.getStart();
            }
        }
        return max;
    }

    /** Method to grab the latest end time of a course in a schedule. */
    private LocalTime getLatest() {
        ArrayList<Timeslot> timeslots = getTimeslots();
        LocalTime max = LocalTime.parse("00:00:00");
        for (Timeslot timeslot : timeslots) {
            if (timeslot.getEnd().compareTo(max) > 0) {
                max = timeslot.getEnd();
            }
        }
        return max;
    }

    public String genTable() {
        String leftAlignFormat = "| %-15s | %-15s | %-15s | %-15s | %-15s |\n";
        String padding =
                "|                |                |                |                |            "
                        + "    |";
        String floor =
                "+----------------+----------------+----------------+----------------+----------------+\n";
        StringBuilder output = new StringBuilder("Schedule: \n\n");
        String[][] arr = populateMatrix();

        // The header for our schedule
        output.append(floor)
                .append(
                        "|     Monday     |     Tuesday    |    Wednesday   |    Thursday    |    "
                                + " Friday     |\n")
                .append(floor);
        for (int i = 0; i < end.getHour() - start.getHour(); i++) {
            String[] col = getColumn(arr, i);
            output.append(padding);
            output.append(String.format(leftAlignFormat, col[0], col[1], col[2], col[3], col[4]));
            output.append(padding);
            output.append(floor);
        }

        return output.toString();
    }
}
