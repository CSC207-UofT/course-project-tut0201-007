package entities;

import java.util.ArrayList;
import java.util.List;

/** This class represents a session, such as a single lecture or tutorial */
public class Section {

    private String name;
    private String session;
    private List<Timeslot> times;

    public Section(String name) {
        this(name, new ArrayList<Timeslot>());
    }

    public Section(String name, List<Timeslot> times) {
        this.name = name;
        this.session = name.split(" ")[2];
        this.times = times;
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
        ret.append(name + " meets at:\n");
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
