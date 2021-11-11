package workers;

import entities.Course;
import entities.Schedule;
import workers.CourseCreator;

import java.io.IOException;
import java.util.ArrayList;

public class Scheduler {
    private final ArrayList courses;
    private final ArrayList schedules;

    /** Constructs a workers.Scheduler with empty courses and schedules */
    public Scheduler() {
        this.courses = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    /**
     * Constructs a workers.Scheduler with the given courses and schedules
     *
     * @param courses list of courses
     * @param schedules list of schedules
     */
    public Scheduler(ArrayList<Course> courses, ArrayList<Schedule> schedules) {
        this.courses = courses;
        this.schedules = schedules;
    }

    /**
     * Takes a list of course codes and outputs a schedule that takes the first lecture session and
     * first tutorial session in each lecture.
     *
     * @param courseCodes an ArrayList of course codes from which a schedule will be generated.
     */
    public Schedule createBasicSchedule(ArrayList<String> courseCodes) {
        Schedule schedule = new Schedule();
        CourseCreator courseCreator = new CourseCreator();
        Course newCourse;

        for (String courseCode : courseCodes) {
            try {
                /**
                 * For every course code, generate the course from workers.CourseCreator, add first lec/tut
                 * session to the lectures and tutorials within the schedule.
                 */
                newCourse = courseCreator.generateCourse(courseCode);
                System.out.println(newCourse);
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
}
