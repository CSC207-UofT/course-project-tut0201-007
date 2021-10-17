import java.util.Scanner;

public class CommandLineInterface {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Boolean input = false;
        int numCourses = 0;
        while (!input) {
            System.out.println("How many courses would you like inforamtion for?");
            try {
                numCourses = Integer.parseInt(scanner.nextLine());
                input = true;

            } catch (Exception NumberFormatException) {
                System.out.println("Please give an integer input");
            }
        }
        CourseCreator b = new CourseCreator();
        for (int a = 0; a < numCourses; a++) {
            System.out.println("Please give the name of one of your courses");
            String course = scanner.nextLine();
            try {
                Course c = b.generateCourse(course);
                System.out.println(c);
            } catch (Exception IOException) {
                System.out.println("Please enter a valid course code");
            }
        }
        scanner.close();
    }
}
