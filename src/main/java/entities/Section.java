package entities;

import java.util.ArrayList;
import java.util.List;

/** This class represents a session, such as a single lecture or tutorial */
public class Section {

    private String name;
    private String session;
    private ArrayList<Timeslot> times;

    public Section(String name) {
        this(name, new ArrayList<Timeslot>());
    }

    public Section(String name, ArrayList<Timeslot> times) {
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

    public List<Timeslot> getTimeslots(){
        return (List<Timeslot>) times.clone();
    }

    public String getSession(){
        return session;
    }

    public String getName(){
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
}
