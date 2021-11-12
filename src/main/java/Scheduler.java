import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public void addFilters(List<Filter> f) {
        this.filters.addAll(f);
    }

    /**
     * Takes a list of course codes and outputs a schedule that takes the first lecture session and
     * first tutorial session in each lecture.
     *
     * @param courseCodes an ArrayList of course codes from which a schedule will be generated.
     */
    public Schedule createBasicSchedule(ArrayList<String> courseCodes) {
        Schedule schedule = new Schedule();
        Course newCourse;

        for (String courseCode : courseCodes) {
            try {
                /**
                 * For every course code, generate the course from CourseCreator, add first lec/tut
                 * session to the lectures and tutorials within the schedule.
                 */
                newCourse = CourseCreator.generateCourse(courseCode, 'F');
                if (!newCourse.getLectures().isEmpty()) {
                    schedule.addLecture(newCourse.getLectures().get(0));
                }
                if (!newCourse.getTutorials().isEmpty()) {
                    schedule.addTutorial(newCourse.getTutorials().get(0));
                }
            } catch (IOException exception) {
                /**
                 * In case something goes wrong with the API for a specific course code, we print
                 * the code and the exception that is thrown.
                 */
                System.out.println(
                        "Exception occured for course "
                                + courseCode
                                + " with the following message: \n"
                                + exception.toString());
            }
        }

        return schedule;
    }

    /**
     * Scheduler used to create courses non recursively by permutation.
     * Courses should be passed so that they are sorted in terms of priority.
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



    /**
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
