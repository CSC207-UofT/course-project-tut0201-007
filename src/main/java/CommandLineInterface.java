import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {

    public CommandLineInterface() {}

    public ArrayList<String> promptUser() {
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
        scanner.close();
        return courses;
        /**
        Scheduler s = new Scheduler();
        List<Course> instantiatedCourses = CommandLineInterface.courseInstantiator(courses);
        List<Schedule> schedules = s.permutationScheduler(instantiatedCourses);
        //System.out.println(s.createBasicSchedule(courses));
         */
    }

}
