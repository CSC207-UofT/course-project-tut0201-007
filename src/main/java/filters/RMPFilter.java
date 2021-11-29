package filters;

import entities.Schedule;
import entities.Section;

class RMPFilter implements Filter {
    Double minimumScore;

    public RMPFilter(Double minimumScore) {
        this.minimumScore = minimumScore;
    }

    @Override
    public boolean checkSchedule(Schedule s) {
        for (Section section : s.getLectures()) {
            if (section.getProfessorRating() < minimumScore) {
                return false;
            }
        }
        return true;
    }
}
