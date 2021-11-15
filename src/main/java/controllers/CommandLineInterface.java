package controllers;

import entities.Course;
import entities.Schedule;
import filters.*;
import workers.ScheduleExporter;

import java.time.LocalTime;
import java.util.*;

/** The user interface of the program. */
public class CommandLineInterface {

    public CommandLineInterface() {}

    /**
     * Method that prompts user for input, eventually returning a List of course IDs that they will
     * be taking. The list should be sorted from greatest to least priority.
     *
     * @return a list of desired course codes from greatest to least priority.
     */
    public static ArrayList<String> promptUser() {
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
        return courses;
    }

    public static List<Filter> promptUserFilters(List<Course> userCourses) {
        ArrayList<Filter> userFilters = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to configure any criteria for your schedules?" +
                "\n" + "1 - Time conflicts" +
                "\n" + "2 - Delivery method" +
                "\n" + "3 - Enforce a time gap between courses" +
                "\n" + "4 - Enforce times when you have no courses" +
                "\n" + "Please enter your choices as valid integer inputs with spaces. (i.e. '1 2 3' or '2' or '')." +
                "A non-integer input will end selection.\n"
        );


        boolean[] filterCodes = new boolean[4];

        while (scanner.hasNextInt()) {
            int index = scanner.nextInt();
            try {
                filterCodes[index-1] = true;
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.println("Invalid integer selection. Please enter an valid option:");
            }
        }

        for(int i = 0; i < filterCodes.length; i++) {
            if (filterCodes[i]) {
                switch (i) {
                    case 0:
                        userFilters.addAll(CommandLineInterface.promptTimeConflictFilter());
                        break;
                    case 1:
                        userFilters.addAll(CommandLineInterface.promptInPersonFilter());
                        break;
                    case 2:
                        userFilters.addAll(CommandLineInterface.promptSpaceFilter());
                        break;
                    case 3:
                        userFilters.addAll(CommandLineInterface.promptTimeFilter());
                        break;
                }
            }
        }

        return userFilters;
    }

    /**
     * Outputs schedules meeting user criteria. User can move through schedules and save them.
     *
     * @param userSchedules schedules meeting filter criteria
     */
    public static void displayUserSchedules(List<Schedule> userSchedules) {
        Scanner scanner = new Scanner(System.in);
        ScheduleExporter scheduleExporter = new ScheduleExporter();
        int numOfSchedules = userSchedules.size() - 1;
        int scheduleNumber = 0;
        char userActivity = 'W';

        while (userActivity != 'Q') {
            Schedule currSchedule = userSchedules.get(scheduleNumber);
            System.out.println("These are schedules meeting your criteria:");
            System.out.println("Schedule No. " + (scheduleNumber + 1) + " / " + (numOfSchedules + 1) + ".");
            System.out.println();
            System.out.println(currSchedule);
            System.out.println();
            System.out.println("Press 'Q' to quit.");
            System.out.println("Press '>' to go to the next schedule.");
            System.out.println("Press '<' to go to the previous schedule.");
            System.out.println("Press 'S' to save this schedule as an .ics file");

            char userInput = scanner.next().charAt(0);
            switch (userInput) {
                case 'Q':
                    userActivity = 'Q';
                    break;
                case '<':
                    if (scheduleNumber > 0) {
                        scheduleNumber--;
                    } else {
                        System.out.println("No schedule before this one.");
                    }
                    break;
                case '>':
                    if (scheduleNumber < numOfSchedules) {
                        scheduleNumber++;
                    } else {
                        System.out.println("No schedules after this one.");
                    }
                    break;
                case 'S':
                    System.out.println("Saving this schedule in .ics format...");
                    ScheduleExporter.outputScheduleICS(currSchedule);
                    break;
            }
        }
    }

/** PRIVATE METHODS BELOW
 *
 *  These guys are used to simplify I/O methods above.
 */
    private static List<Filter> promptTimeConflictFilter(){
        Scanner scanner = new Scanner(System.in);
        ArrayList<Filter> newFilters = new ArrayList<>();
        System.out.println("Would you like to allow time conflicts between your courses? \n" +
                "Enter 0/1 for N/Y. \n" +
                "A non-integer input will quit selection.");

        while (scanner.hasNextInt()) {
            int input = scanner.nextInt();
            if (input == 0) {
                newFilters.add(new ConflictFilter());
                System.out.println("Schedules with time conflicts will be REMOVED.");
                return newFilters;
            } else if (input == 1) {
                System.out.println("Schedules with time conflicts will be ALLOWED.");
                return newFilters;
            }
        }
        return newFilters;
    }

    private static List<Filter> promptInPersonFilter(){
        Scanner scanner = new Scanner(System.in);
        ArrayList<Filter> newFilters = new ArrayList<>();
        System.out.println("Would you like all courses online or in-person? \n" +
                "Enter 0/1 for online/in-person. \n" +
                "A non-integer input will quit selection.");

        while (scanner.hasNextInt()) {
            int input = scanner.nextInt();
            if (input == 1) {
                newFilters.add(new InPersonFilter(true));
                System.out.println("Schedules with ONLINE courses will be REMOVED.");
                return newFilters;
            } else if (input == 0) {
                newFilters.add(new InPersonFilter(false));
                System.out.println("Schedules with IN-PERSON will be REMOVED.");
                return newFilters;
            }
        }
        return newFilters;
    }

    private static List<Filter> promptSpaceFilter(){
        Scanner scanner = new Scanner(System.in);
        ArrayList<Filter> newFilters = new ArrayList<>();
        System.out.println("Would you like to enforce a time gap between all courses? \n" +
                "Enter an integer for the number of hours in the time gap. \n" +
                "A non-integer input will quit selection.");

        if (scanner.hasNextInt()) {
            int gap = scanner.nextInt();
            newFilters.add(new SpaceFilter(gap));
            System.out.println("ONLY schedules with " + 1 + "hour gap between classes will be generated.");
            return newFilters;
        }
        System.out.println("You did not specify a time gap. Quitting selection.");
        return newFilters;
    }

    private static List<Filter> promptTimeFilter(){
        Scanner scanner = new Scanner(System.in);
        ArrayList<Filter> newFilters = new ArrayList<>();
        TimeFilter.Day[] days = {
                TimeFilter.Day.ALL_DAYS, TimeFilter.Day.MONDAY, TimeFilter.Day.TUESDAY, TimeFilter.Day.WEDNESDAY,
                TimeFilter.Day.THURSDAY, TimeFilter.Day.FRIDAY
        };
        String[] dayStrings = {
                "Everyday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
        };

        System.out.println("Would you like to specify times during which you have courses? " +
                "(Enter 0/1 for N/Y). " +
                "A non-integer input will quit selection.");
        int input = 0;
        if (scanner.hasNextInt()) {
            input = scanner.nextInt();
        }


        while (input == 1) {
            System.out.println("During which day do you want to include times?\n" +
                    "(0/1/2/3/4/5 for Everyday/Mon./Tue./Wed./Thu./Fri.)");
            int day = scanner.nextInt();
            while (!(day >= 0 && day <= 5)) {
                System.out.println("Invalid input. Please enter another integer.");
                day = scanner.nextInt();
            }

            System.out.println("From what time during these day(s) do you want classes?");
            LocalTime startTime = CommandLineInterface.timeInputHandler();
            System.out.println("Until what time during these day(s) do you want classes?");
            LocalTime endTime = CommandLineInterface.timeInputHandler();

            if(startTime.compareTo(endTime) > 0) {
                System.out.println("Your start time is before your end time." +
                        " Please try again during the next iteration.");
            } else {
                //WHY DOES DAY NOT HAVE TO STRING METHOD???? quick fix for now by hardcoding an array
                System.out.println("You would like classes during " + dayStrings[day] + " from " +
                        startTime.toString() + " until " + endTime.toString());
                System.out.println("Is the above correct? (0/1 for N/Y).");
                int sc = scanner.nextInt();
                if (sc == 1) {
                    newFilters.add(new TimeFilter(startTime, endTime, days[day]));
                } else if (sc == 0) {
                    System.out.println("Please try again on the next iteration.");
                } else {
                    System.out.println("Invalid input. Please try again on the next iteration.");
                }
            }
            System.out.println("Would you like to restrict your schedule to another block of time? (0/1 for N/Y). \n" +
                    "Non-integer input will quit selection, and blocks will NOT be added to scheduling.");

            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input == 1) {
                    System.out.println("Looping...");
                } else if (input == 0) {
                    System.out.println("Your selected blocks of time are saved and courses taking place outside these times " +
                            "will be excluded. Exiting selection.");
                    return newFilters;
                } else {
                    System.out.println("Your selected times will not be included in schedule generation. Exiting selection.");
                    return new ArrayList<>();
                }
            } else {
                    System.out.println("Your selected times will not be included in schedule generation. Exiting selection.");
                    return new ArrayList<>();
            }


        }
        if (input == 0) {
            System.out.println("Input not selected.");
        }
        return new ArrayList<>();
    }

    private static LocalTime timeInputHandler() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please use hh:mm format in 24 hour time.");
        while (true) {
            String[] input = scanner.next().split(":| +");
            try {
                int hours = Integer.parseInt(input[0]);
                int mins = Integer.parseInt(input[1]);
                return LocalTime.of(hours, mins, 0, 0);
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.println("Invalid input. PLEASE use HH:MM format in 24 HOUR TIME:");
            }
        }
    }
}
