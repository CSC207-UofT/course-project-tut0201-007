import java.util.ArrayList;

public class Scheduler {

    private final ArrayList courses;
    private final ArrayList schedules;

    public Scheduler() {
        this.courses = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    public Scheduler(ArrayList<Course> courses, ArrayList<Schedule> schedules) {
        this.courses = courses;
        this.schedules = schedules;
    }

    public Schedule generateSchedule() {
        return new Schedule();
    }


}
