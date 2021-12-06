package filters;

import entities.Schedule;
import entities.Section;

public class ProfessorRatingFilter implements Filter {
    double minimumScore;

    public ProfessorRatingFilter(double minimumScore) {
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
