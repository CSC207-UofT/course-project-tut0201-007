import java.util.ArrayList;

/**
 * This class represents a distinct collection of sessions. Class Scheduler stores these in order to
 * keep track of possible schedules.
 */
public class Schedule {

    private final ArrayList<Section> lectures;
    private final ArrayList<Section> tutorials;

    /** No parameter constructor that creates empty ArrayLists of lecture and tutorial sessions. */
    public Schedule() {
        this.lectures = new ArrayList<>();
        this.tutorials = new ArrayList<>();
    }

    /**
     * Constructor that assigns ArrayLists of lecture and tutorial sessions.
     *
     * @param lectures is the set of lecture sessions in this schedule
     * @param tutorials is the set of tutorial sessions in this schedule
     */
    public Schedule(ArrayList<Section> lectures, ArrayList<Section> tutorials) {
        this.lectures = lectures;
        this.tutorials = tutorials;
    }

    public ArrayList<Section> getLectures() {
        return lectures;
    }

    public ArrayList<Section> getTutorials() {
        return tutorials;
    }

    /**
     * Mutate the lectures list by adding lecture sessions in chronological order
     *
     * @param lecture is a lecture session
     */
    public void addLecture(Section lecture) {
        lectures.add(lecture);
    }

    /**
     * Mutate the tutorials list by adding lecture sessions in chronological order
     *
     * @param tutorial is a tutorial session
     */
    public void addTutorial(Section tutorial) {
        tutorials.add(tutorial);
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder("Schedule: \n\n");
        representation.append("Lectures\n");
        for (Section s : this.lectures) {
            representation.append(s.toString()).append("\n");
        }
        representation.append("\nTutorials\n");
        for (Section s : this.tutorials) {
            representation.append(s.toString()).append("\n");
        }
        return representation.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Schedule) {
            return this.lectures.equals(((Schedule) o).getLectures())
                    && this.tutorials.equals(((Schedule) o).getTutorials());
        }
        return false;
    }
}
