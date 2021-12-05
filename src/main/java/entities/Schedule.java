package entities;

import java.util.ArrayList;

/**
 * This class represents a distinct collection of sessions. Class workers.Scheduler stores these in
 * order to keep track of possible schedules.
 */
public class Schedule implements Cloneable {

    private final ArrayList<Section> lectures;
    private final ArrayList<Section> tutorials;
    private final ArrayList<String> courses;
    private final ArrayList<Timeslot> timeslots;

    /** No parameter constructor that creates empty ArrayLists of lecture and tutorial sessions. */
    public Schedule() {
        this.lectures = new ArrayList<>();
        this.tutorials = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.timeslots = new ArrayList<>();
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
        this.courses = new ArrayList<>();
        this.timeslots = populateTimeslots();

        for (Section sec : this.lectures) {
            String courseCode = sec.getName().substring(0,6);
            if (!this.courses.contains(courseCode)) {
                courses.add(courseCode);
            }
        }
    }

    public ArrayList<Section> getLectures() {
        return lectures;
    }

    public ArrayList<Section> getTutorials() {
        return tutorials;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public ArrayList<Timeslot> getTimeslots() {
        return timeslots;
    }

    /**
     * Creates a total list of timeslots.
     *
     * We end up iterating over timeslots as a whole a fair amount, so we might as well do it once.
     */
    private ArrayList<Timeslot> populateTimeslots () {

        ArrayList<Timeslot> timeslots = new ArrayList();

        for (Section lec : this.lectures) {
            timeslots.addAll(lec.getTimes());
        }

        for (Section tut : this.tutorials) {
            timeslots.addAll(tut.getTimes());
        }

        return timeslots;
    }

    /**
     * Mutate the lectures list by adding lecture sessions in chronological order
     *
     * @param lecture is a lecture session
     */
    public void addLecture(Section lecture) {
        lectures.add(lecture);
        timeslots.addAll(lecture.getTimes());

        String courseCode = lecture.getName().substring(0,6);
        if (!this.courses.contains(courseCode)) {
            this.courses.add(courseCode);
        }
    }

    /**
     * Mutate the tutorials list by adding lecture sessions in chronological order
     *
     * @param tutorial is a tutorial session
     */
    public void addTutorial(Section tutorial) {
        tutorials.add(tutorial);
        timeslots.addAll(tutorial.getTimes());
    }

    @Override
    public String toString() {
//        StringBuilder representation = new StringBuilder("Schedule: \n\n");
//        representation.append("Lectures\n");
//        for (Section s : this.lectures) {
//            representation.append(s.toString()).append("\n");
//        }
//        representation.append("\nTutorials\n");
//        for (Section s : this.tutorials) {
//            representation.append(s.toString()).append("\n");
//        }
//        return representation.toString();

        ASCIIFormatter ascii = new ASCIIFormatter(this);
        return ascii.genTable();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Schedule) {
            return this.lectures.equals(((Schedule) o).getLectures())
                    && this.tutorials.equals(((Schedule) o).getTutorials());
        }
        return false;
    }

    @Override
    public Schedule clone() {
        return new Schedule(
                (ArrayList<Section>) this.lectures.clone(),
                (ArrayList<Section>) this.tutorials.clone()
        );
    }
}
