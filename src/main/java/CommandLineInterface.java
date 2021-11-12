import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Boolean input = false;
        int numCourses = 0;
        while (!input) {
            System.out.println("How many courses would you like information for?");
            try {
                numCourses = Integer.parseInt(scanner.nextLine());
                input = true;

            } catch (Exception NumberFormatException) {
                System.out.println("Please give an integer input");
            }
        }
        ArrayList<String> courses = new ArrayList<>();
        for (int a = 0; a < numCourses; a++) {
            System.out.println("Please give the course code of one of your courses");
            String course = scanner.nextLine();
            courses.add(course);
        }
        Scheduler s = new Scheduler();
        List<Course> instantiatedCourses = CommandLineInterface.courseInstantiator(courses);
        List<Schedule> schedules = s.permutationScheduler(instantiatedCourses);
        //System.out.println(s.createBasicSchedule(courses));
        scanner.close();

        for(Schedule sch : schedules) {
            System.out.println(sch);
        }
    }

    private static List<Course> courseInstantiator(ArrayList<String> courseCodes) {
        ArrayList<Course> courses = new ArrayList<>();
        for (String courseCode : courseCodes) {
            try {
                /**
                 * For every course code, generate the course from CourseCreator, add first lec/tut
                 * session to the lectures and tutorials within the schedule.
                 */
                Course newCourse = CourseCreator.generateCourse(courseCode, 'F');
                courses.add(newCourse);
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
        return courses;
    }
}
