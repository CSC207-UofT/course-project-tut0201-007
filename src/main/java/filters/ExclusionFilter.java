package filters;

import entities.Schedule;

public class ExclusionFilter implements Filter {

    public ExclusionFilter() {

    }

    @Override
    public Boolean checkSchedule(Schedule s) {

        if (s == null) {
            return false;
        }

        return true;

    }
}
