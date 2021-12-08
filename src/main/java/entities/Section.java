package entities;

import java.util.ArrayList;
import java.util.List;

/** This class represents a section, such as a single lecture or tutorial */
public class Section {

    private final String name;
    private final String session;
    private final List<Timeslot> times;
    private Double professorRating;

    public Section(String name) {
        this(name, new ArrayList<Timeslot>(), 2.5);
    }

    public Section(String name, Double professorRating) {
        this(name, new ArrayList<Timeslot>(), professorRating);
    }

    public Section(String name, List<Timeslot> times) {
        this(name, times, 2.5);
    }

    public Section(String name, List<Timeslot> times, Double professorRating) {
        this.name = name;
        this.session = name.split(" ")[2];
        this.times = times;
        this.professorRating = professorRating;
    }

    /**
     * adds a timeslot to a session's list of times
     *
     * @param t a timeslot to be added to a lec/tut
     */
    public void addTime(Timeslot t) {
        times.add(t);
    }

    public List<Timeslot> getTimes() {
        return List.copyOf(times);
    }

    public String getSession() {
        return session;
    }

    public String getName() {
        return name;
    }

    public Double getProfessorRating() {
        return professorRating;
    }

    public boolean checkConflict(Section other) {
        for (Timeslot t : times) {
            for (Timeslot a : other.times) {
                if (t.conflictsWith(a)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (times.size() == 0) return name + " is ASYNC";
        StringBuilder ret = new StringBuilder();
        ret.append(
                name + " has a professor rating of " + getProfessorRating() + " and meets at:\n");
        for (Timeslot s : times) {
            ret.append(s.toString() + "\n");
        }
        return ret.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Section) {
            Section other = (Section) o;
            return this.name.equals(other.getName())
                    && this.session.equals(other.getSession())
                    && this.getTimes().equals(other.getTimes());
        }
        return false;
    }
}
