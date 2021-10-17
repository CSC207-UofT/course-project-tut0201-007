import java.io.IOException;
import java.util.ArrayList;

public class Scheduler {
    private final ArrayList courses;
    private final ArrayList schedules;

    /**
     * Constructs a Scheduler with empty courses and schedules
     */
    public Scheduler() {
        this.courses = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    /**
     * Constructs a Scheduler with the given courses and schedules
     * @param courses list of courses
     * @param schedules list of schedules
     */
    public Scheduler(ArrayList<Course> courses, ArrayList<Schedule> schedules) {
        this.courses = courses;
        this.schedules = schedules;
    }

    /**
     * Takes a list of course codes and outputs a schedule that takes the first lecture session and first
     * tutorial session in each lecture.
     *
     * @param courseCodes an ArrayList of course codes from which a schedule will be generated.
     */
    public Schedule createBasicSchedule(ArrayList<String> courseCodes) {
        Schedule schedule = new Schedule();
        CourseCreator courseCreator = new CourseCreator();
        Course new_course;

        for (String courseCode : courseCodes) {
            try {
                /**
                 * For every course code, generate the course from CourseCreator, add first lec/tut session to
                 * the lectures and tutorials within the schedule.
                 */
                new_course = courseCreator.generateCourse(courseCode);
                System.out.println(new_course);
                if(!new_course.getLectures().isEmpty()){
                    schedule.addLecture(new_course.getLectures().get(0));
                }
                else if(!new_course.getTutorials().isEmpty()){
                    schedule.addTutorial(new_course.getTutorials().get(0));
                }
            } catch (IOException exception) {
                /**
                 * In case something goes wrong with the API for a specific course code, we print the code
                 * and the exception that is thrown.
                 */
                System.out.println("Exception occured for course " + courseCode + " with the following message: \n"
                        + exception.toString());
            }
        }

        return schedule;
    }


}
