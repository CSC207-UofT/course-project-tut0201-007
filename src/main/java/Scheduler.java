import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for scheduling courses with different criteria.
 *
 * Key method is permutationScheduler() which creates all permutations passing filters.
 */
public class Scheduler {
    private final List<Course> courses;
    private final List<Schedule> schedules;
    private final List<Filter> filters = new ArrayList<Filter>();

    /** Constructs a Scheduler with empty courses and schedules */
    public Scheduler() {
        this.courses = new ArrayList<Course>();
        this.schedules = new ArrayList<Schedule>();
    }

    /**
     * Constructs a Scheduler with the given courses and schedules
     *
     * @param courses list of courses
     * @param schedules list of schedules
     */
    public Scheduler(ArrayList<Course> courses, ArrayList<Schedule> schedules) {
        this.courses = courses;
        this.schedules = schedules;
    }

    public void addFilters(List<Filter> filters) {
        this.filters.addAll(filters);
    }

    /**
     * Scheduler used to create courses all course permutations recursively. During generation, schedulers not passing
     * filters are removed in the populatePermutations and extendPermutations().
     *
     * @param newCourses courses sorted by priority to be scheduled.
     */
    public List<Schedule> permutationScheduler(List<Course> newCourses) {
        if (newCourses.isEmpty()) {
            return new ArrayList<>();
        }
        int numOfCourses = newCourses.size();

        if (newCourses.size() == 1) {
            Course newCourse = newCourses.get(0);
            List<Schedule> newSchedules = populatePermutations(newCourse);
            return newSchedules;
        } else {
            Course newCourse = newCourses.get(numOfCourses-1);
            newCourses.remove(numOfCourses-1);
            List<Schedule> savedSchedules = permutationScheduler(newCourses);
            List<Schedule> newSchedules = new ArrayList<>();

            for (Schedule schedule : savedSchedules) {
                newSchedules.addAll(extendPermutations(newCourse, schedule));
            }

            savedSchedules.addAll(newSchedules);
            return savedSchedules;
        }
    }

    /**
     * Takes a list of courses and outputs a schedule that takes the first lecture section and
     * first tutorial section in each course. Mainly used for testing purposes.
     *
     * @param courses an ArrayList of courses from which a schedule will be generated.
     */
    public Schedule createBasicSchedule(ArrayList<Course> courses) {
        Schedule schedule = new Schedule();

        for (Course newCourse : courses) {
            if (!newCourse.getLectures().isEmpty()) {
                schedule.addLecture(newCourse.getLectures().get(0));
            }
            if (!newCourse.getTutorials().isEmpty()) {
                schedule.addTutorial(newCourse.getTutorials().get(0));
            }
        }
        return schedule;
    }

    /** Creates all lecture/tutorial section permutations that pass all filters for one course.
     *
     * @param c course that we take lec/tut permutations of
     * @return all filtered schedules with these lec/tut permutations
     */
    private List<Schedule> populatePermutations(Course c) {
        List<Schedule> populatedSchedules = new ArrayList<>();
        List<Section> courseLectures = c.getLectures();
        List<Section> courseTutorials = c.getTutorials();

        for (Section lec : courseLectures) {
            for(Section tut : courseTutorials) {
                Schedule tempSchedule = new Schedule();
                tempSchedule.addLecture(lec);
                tempSchedule.addTutorial(tut);

                if (this.checkFilters(tempSchedule)) {
                    populatedSchedules.add(tempSchedule);
                }
            }
        }

        return populatedSchedules;
    }

    /** Returns permutations of lectures and tutorials of course c, passing filters, together
     * with all sections already present in schedule s.
     *
     * @param c the course whose lecs/tuts will be permuted
     * @param s the schedule to which the permutations will be added
     * @return all added permutations to this schedule
     */
    private List<Schedule> extendPermutations(Course c, Schedule s) {
        List<Schedule> populatedSchedules = new ArrayList<>();
        List<Section> courseLectures = c.getLectures();
        List<Section> courseTutorials = c.getTutorials();

        for (Section lec : courseLectures) {
            for(Section tut : courseTutorials) {
                Schedule tempSchedule = s.clone();
                tempSchedule.addLecture(lec);
                tempSchedule.addTutorial(tut);

                if (this.checkFilters(tempSchedule)) {
                    populatedSchedules.add(tempSchedule);
                }
            }
        }
        return populatedSchedules;
    }


    /** Checks filters for this scheduler
     *
     * @return true if all filters pass for Schedule given to the method
     */
    private boolean checkFilters(Schedule s) {
        for (Filter f : this.filters) {
            if (f.checkSchedule(s) == null) {
                return false;
            }
        }
        return true;
    }

}
