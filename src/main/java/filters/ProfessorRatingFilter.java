package filters;

import entities.Schedule;
import entities.Section;

class ProfessorRatingFilter implements Filter {
    Double minimumScore;

    public ProfessorRatingFilter(Double minimumScore) {
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
