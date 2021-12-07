package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a distinct collection of sessions. Class workers.Scheduler stores these in
 * order to keep track of possible schedules.
 */
public class Schedule implements Cloneable {

    private final List<Section> lectures;
    private final List<Section> tutorials;
    private final List<String> courses;

    /** No parameter constructor that creates empty ArrayLists of lecture and tutorial sessions. */
    public Schedule() {
        this.lectures = new ArrayList<>();
        this.tutorials = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    /**
     * Constructor that assigns ArrayLists of lecture and tutorial sessions.
     *
     * @param lectures is the set of lecture sessions in this schedule
     * @param tutorials is the set of tutorial sessions in this schedule
     */
    public Schedule(List<Section> lectures, List<Section> tutorials) {
        this.lectures = lectures;
        this.tutorials = tutorials;
        this.courses = new ArrayList<>();

        for (Section sec : this.lectures) {
            String courseCode = sec.getName().substring(0, 6);
            if (!this.courses.contains(courseCode)) {
                courses.add(courseCode);
            }
        }
    }

    public List<Section> getLectures() {
        return lectures;
    }

    public List<Section> getTutorials() {
        return tutorials;
    }

    public List<String> getCourses() {
        return courses;
    }

    /**
     * Mutate the lectures list by adding lecture sessions in chronological order
     *
     * @param lecture is a lecture session
     */
    public void addLecture(Section lecture) {
        lectures.add(lecture);

        String courseCode = lecture.getName().substring(0, 6);
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

    public boolean isEmpty() {
        return lectures.isEmpty();
    }

    @Override
    public Schedule clone() {
        return new Schedule(new ArrayList(lectures), new ArrayList(tutorials));
    }
}