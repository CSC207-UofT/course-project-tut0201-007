package schedule;

import java.util.ArrayList;

/**
 * This class represents a distinct collection of sessions. Class schedule.Scheduler stores these in order to
 * keep track of possible schedules.
 */
public class Schedule {

    private final ArrayList<Session> lectures;
    private final ArrayList<Session> tutorials;

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
    public Schedule(ArrayList<Session> lectures, ArrayList<Session> tutorials) {
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
     * Mutate the lectures list by adding lecture sessions in chronological order
     *
     * @param lecture is a lecture session
     */
    public void addLecture(Session lecture) {
        if (this.lectures.isEmpty()) {
            this.lectures.add(lecture);
        } else if (this.lectures.size() == 1) {
            // if lecture in list is before the lecture to be added
            if (this.lectures.get(0).compareTo(lecture) < 0) {
                this.lectures.add(lecture);
            } else {
                this.lectures.add(0, lecture);
            }
        } else {
            for (int i = 0; i < this.lectures.size(); i++) {
                if (this.lectures.get(i).compareTo(lecture) < 0 && i != this.lectures.size() - 1) {
                    this.lectures.add(i + 1, lecture);
                    return;
                }
            }
            this.lectures.add(lecture);
        }
    }

    /**
     * Mutate the tutorials list by adding lecture sessions in chronological order
     *
     * @param tutorial is a tutorial sesstion
     */
    public void addTutorial(Session tutorial) {
        if (this.tutorials.isEmpty()) {
            this.tutorials.add(tutorial);
        } else if (this.tutorials.size() == 1) {
            // if tutorial in list is before the tutorial to be added
            if (this.tutorials.get(0).compareTo(tutorial) < 0) {
                this.tutorials.add(tutorial);
            } else {
                this.tutorials.add(0, tutorial);
            }
        } else {
            for (int i = 0; i < this.tutorials.size(); i++) {
                if (this.tutorials.get(i).compareTo(tutorial) < 0
                        && i != this.lectures.size() - 1) {
                    this.tutorials.add(i + 1, tutorial);
                    return;
                }
            }
            this.tutorials.add(tutorial);
        }
    }

    @Override
    public String toString() {
        return "schedule.Schedule{" + "lectures=" + lectures + ", tutorials=" + tutorials + '}';
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
