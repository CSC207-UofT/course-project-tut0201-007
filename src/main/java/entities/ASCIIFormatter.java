package entities;

import java.time.LocalTime;
import java.util.*;

public class ASCIIFormatter {

    private final Schedule schedule;
    private final LocalTime start;
    private final LocalTime end;
    private final LocalTime latest;
    private final Set<Timeslot> timeslots;
    private final boolean[] session;
    private final HashMap<Timeslot, String> courses;

    /**
     * Constructor that takes schedule to be turned into ASCII output.
     *
     * @param sched is the schedule to be formatted
     */
    public ASCIIFormatter(Schedule sched) {
        this.schedule = sched;
        this.courses = populateTimeslots();
        this.timeslots = populateTimeslots().keySet();
        this.start = getEarly();
        this.end = getLate();
        this.latest = getLatest();
        this.session = checkSession();
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
     * getter method for latest
     *
     * @return latest end time in the schedule
     */
    public LocalTime getEndLate() {
        return latest;
    }

    /**
     * getter method for timeslots
     *
     * @returnlist of timeslots
     */
    public Set<Timeslot> getTimeslots() {
        return timeslots;
    }

    /**
     * Check which sessions we need for however many schedules
     *
     * @return list of Booleans (0 is for F, 1 is for S)
     */
    public boolean[] checkSession() {
        boolean[] temp = new boolean[2];
        for (Timeslot timeslot : timeslots) {
            if (timeslot.getSession() == 'F') {
                temp[0] = true;
            } else if (timeslot.getSession() == 'S') {
                temp[1] = true;
            }
        }

        return temp;
    }

    /**
     * getter method for timeslots, while making a mapping for each timeslot's course code
     *
     * @return array list of all timeslots in the schedule (in no particular order)
     */
    public HashMap<Timeslot, String> populateTimeslots() {
        HashMap<Timeslot, String> courseCodes = new HashMap<>();

        for (Section lec : this.schedule.getLectures()) {
            for (Timeslot timeslot : lec.getTimes()) {
                String name = lec.getName();
                // map the timeslot w/ the name i.e. MAT237
                courseCodes.put(timeslot, name.split(" ")[0]);
            }
        }
        // you can have empty tutorials
        if (!this.schedule.getTutorials().isEmpty()) {
            for (Section tut : this.schedule.getTutorials()) {
                for (Timeslot timeslot : tut.getTimes()) {
                    String name = tut.getName();
                    // map the timeslot w/ the name i.e. MAT237
                    courseCodes.put(timeslot, name.split(" ")[0]);
                }
            }
        }

        return courseCodes;
    }

    /**
     * Method to turn our timeslots into a nx5 array that can be used to construct our ascii
     * schedule.
     *
     * <p>The array needs to have 5 columns, and variable height based on the hours slots we have.
     * Each element is a Timeslot, which will be used in genTable(). Note that the same timeslot
     * appears multiple times if the timeslot is longer than an hour.
     *
     * @param session this is the specific session we are rendering the schedule for
     * @return nx5 matrix of either empty strings or timeslots for wherever they are in the schedule
     *     (relatively)
     */
    public String[][] populateMatrix(char session) {
        // height is bounded by the latest time and earliest time
        String[][] mat = new String[latest.getHour() - start.getHour()][5];

        // pre populate matrix
        for (String[] strings : mat) {
            Arrays.fill(strings, "");
        }

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                for (Timeslot timeslot : timeslots) {
                    // day - 1 because Monday needs to be 0 not 1
                    if (j == (timeslot.getDay().getValue() - 1)
                            && i == (timeslot.getStart().getHour() - start.getHour())
                            && timeslot.getSession() == session) {
                        // ie if 2 hour long lec then curr cell and cell below should get filled w/
                        // same timeslot
                        for (int l = 0;
                                l < timeslot.getEnd().getHour() - timeslot.getStart().getHour();
                                l++) {
                            mat[i + l][j] = courses.get(timeslot) + " " + timeslot.quickString();
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
        // regex for our variable row
        String leftAlignFormat = "| %-24s | %-24s | %-24s | %-24s | %-24s |\n";
        // padding for top and bottom of cells
        String padding =
                "|                          |                          |                         "
                        + " |                          |                          |\n";
        // divide for each row
        String floor =
                "+--------------------------+--------------------------+--------------------------+--------------------------+--------------------------+\n";

        StringBuilder output = new StringBuilder("Schedule(s):\n");
        if (session[0]) {
            output.append("F -> \n");
            String[][] arrF = populateMatrix('F');
            output.append(floor)
                    .append(
                            "|          Monday          |          Tuesday         |        "
                                + " Wednesday        |         Thursday         |          Friday "
                                + "         |\n")
                    .append(floor);
            for (int i = 0; i < latest.getHour() - start.getHour(); i++) {
                output.append(padding);
                output.append(
                        String.format(
                                leftAlignFormat,
                                arrF[i][0],
                                arrF[i][1],
                                arrF[i][2],
                                arrF[i][3],
                                arrF[i][4]));
                output.append(padding);
                output.append(floor);
            }
        }

        if (session[1]) {
            output.append("S -> \n");
            String[][] arrS = populateMatrix('S');
            output.append(floor)
                    .append(
                            "|          Monday          |          Tuesday         |        "
                                + " Wednesday        |         Thursday         |          Friday "
                                + "         |\n")
                    .append(floor);
            for (int i = 0; i < latest.getHour() - start.getHour(); i++) {
                output.append(padding);
                output.append(
                        String.format(
                                leftAlignFormat,
                                arrS[i][0],
                                arrS[i][1],
                                arrS[i][2],
                                arrS[i][3],
                                arrS[i][4]));
                output.append(padding);
                output.append(floor);
            }
        }

        return output.toString();
    }
}
