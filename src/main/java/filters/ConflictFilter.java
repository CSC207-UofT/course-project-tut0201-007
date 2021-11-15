package filters;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.util.ArrayList;
import java.util.Collections;

public class ConflictFilter implements Filter {

    @Override
    public boolean checkSchedule(Schedule s) {
        if (s == null) {
            return false;
        }

        ArrayList<Timeslot> timeslots = new ArrayList();

        for (Section lec : s.getLectures()) {
            timeslots.addAll(lec.getTimes());
        }

        for (Section tut : s.getTutorials()) {
            timeslots.addAll(tut.getTimes());
        }

        Collections.sort(timeslots);


        for (int i = 0; i < timeslots.size() - 1; i++) {
            Timeslot current = timeslots.get(i);
            Timeslot next = timeslots.get(i + 1);
            if (current.conflictsWith(next)) {
                return false;
            }
        }

        return true;
    }
}
