import java.util.ArrayList;

/**
 * This class represents a distinct collection of sessions.
 * Class Scheduler stores these in order to keep track of possible schedules.
 */

public class Schedule {

    //TODO: Make sessions comparable by time and put these in chronological order.
    /**
     * This is important as it makes Schedules easily comparable,
     * and keeps the insertion of new sessions easy.
     */

    private final ArrayList<Session> lectures;
    private final ArrayList<Session> tutorials;

    /**
     * No parameter constructor that creates empty ArrayLists of lecture and tutorial
     * sessions.
     */
    public Schedule () {
        this.lectures = new ArrayList<>();
        this.tutorials = new ArrayList<>();
    }

    /**
     * Constructor that assigns ArrayLists of lecture and tutorial sessions.
     * @param lectures is the set of lecture sessions in this schedule
     * @param tutorials is the set of tutorial sessions in this schedule
     */
    public Schedule(ArrayList<Session> lectures, ArrayList<Session> tutorials){
        this.lectures = lectures;
        this.tutorials = tutorials;
    }

    public ArrayList<Session> getLectures() {
        return lectures;
    }

    public ArrayList<Session> getTutorials() {
        return tutorials;
    }


    /**
     * String representation of this class.
     * @return The string representation of the class.
     */
    @Override
    public String toString() {
        return "Schedule{" +
                "lectures=" + lectures +
                ", tutorials=" + tutorials +
                '}';
    }

    /**
     * Returns true if two Schedule objects have the same lecture and tutorial Sessions.
     *
     * Checks equality of the instances of ArrayList between 'this' and 'o'.
     * @param o is an arbitrary object
     */
    @Override
    public boolean equals(Object o){
        if (o instanceof Schedule) {
            return this.lectures.equals(((Schedule) o).getLectures()) &&
                    this.tutorials.equals(((Schedule) o).getTutorials());
        }
        return false;
    }
}
