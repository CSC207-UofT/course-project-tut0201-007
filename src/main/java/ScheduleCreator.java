import java.io.IOException;
import java.util.ArrayList;

/**
 * A class that creates a schedule given a set of courses.
 * For the skeleton code, the class takes course codes and returns a schedule with the first lecture and tutorial
 * session added to the schedule.
 */
public class ScheduleCreator {

    /**
     * Empty constructor since it is almost unnecessary.
     */
    public ScheduleCreator() {}

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
                schedule.addLecture(new_course.getLectures().get(0));
                schedule.addTutorial(new_course.getTutorials().get(0));
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
