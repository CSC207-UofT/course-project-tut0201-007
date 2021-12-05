package controllers;

import entities.Course;
import entities.Schedule;
import filters.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.ConsoleColours;
import util.PromptHelpers;
import workers.CSVExporter;
import workers.Exporter;
import workers.ICSExporter;
import workers.ICSImporter;

/** The user interface of the program. */
public class CommandLineInterface {

    PromptHelpers promptHelpers = new PromptHelpers();

    public CommandLineInterface() {}

    private GenerationMode generationMode;

    /**
     * Constructor.
     *
     * @param mode represents one by one generation for the Controller
     *     <p>if one by one generation is used in controller, displayUserSchedule will take input to
     *     return a schedule
     */
    public CommandLineInterface(GenerationMode mode) {
        generationMode = mode;
    }

    /**
     * Gets generation mode.
     *
     * @return generationMode
     */
    public GenerationMode getGenerationMode() {
        return generationMode;
    }

    /**
     * Sets generation mode.
     *
     * @param mode must be enum ONE_BY_ONE or ALL_PERMUTATIONS as described in enum class
     *     GenerationMode
     */
    public void setGenerationMode(GenerationMode mode) {
        generationMode = mode;
    }

    /**
     * Prompts user for input, asking whether they would like to import a schedule or make a new
     * one. Informs Controller how to perform generation.
     *
     * @return an integer representing whether the user wants to import or creates a new schedule 0
     *     -> import 1 -> new schedule other integer -> exit program
     */
    public int promptUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(ConsoleColours.BLUE_BOLD_BRIGHT);
        System.out.println("=== We Do A Little Scheduling :) ===");
        System.out.print(ConsoleColours.RESET);
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Would you like to create a new schedule, or import a schedule to"
                        + " configure? ---\n"
                        + ConsoleColours.RESET
                        + " • Press 1 to"
                        + ConsoleColours.BLUE
                        + " create a new schedule. \n"
                        + ConsoleColours.RESET
                        + " • Press 0 to"
                        + ConsoleColours.BLUE
                        + " import a schedule. \n"
                        + ConsoleColours.RESET
                        + "Press 'Q' to quit selection.");

        int inputInt;
        while (scanner.hasNextInt()) {
            inputInt = scanner.nextInt();
            if (inputInt == 1) {
                System.out.println(
                        ConsoleColours.GREEN + "Creating new schedules..." + ConsoleColours.RESET);
                return 1;
            } else if (inputInt == 0) {
                System.out.println(
                        ConsoleColours.GREEN + "Importing schedule..." + ConsoleColours.RESET);
                return 0;
            } else {
                System.out.print(ConsoleColours.RED);
                System.out.println("Please select a valid integer.");
                System.out.print(ConsoleColours.RESET);
            }
        }
        return -1;
    }

    /**
     * Asks the user to enter the course codes they would like to include in schedule generation.
     *
     * @return a String list of course codes
     */
    public List<String> promptCourseCodeNames() {
        Scanner scanner = new Scanner(System.in);
        boolean input = false;
        int numCourses = 0;
        while (!input) {
            System.out.println(
                    ConsoleColours.WHITE_BOLD_BRIGHT
                            + "--- How many courses would you like to add? ---"
                            + ConsoleColours.RESET);
            try {
                numCourses = Integer.parseInt(scanner.nextLine());
                input = true;

            } catch (Exception NumberFormatException) {
                System.out.print(ConsoleColours.RED);
                System.out.println("Please give an integer input");
                System.out.print(ConsoleColours.RESET);
            }
        }
        List<String> courses = new ArrayList<>();
        Pattern validInput = Pattern.compile("^[a-zA-Z0-9]{6}[fsyFSY]");
        int a = 0;
        while (a < numCourses) {
            System.out.println(
                    ConsoleColours.WHITE_BOLD_BRIGHT
                            + "--- Please give the course code and session of one of your courses."
                            + " --- \n"
                            + ConsoleColours.RESET
                            + "An example of expected format is "
                            + ConsoleColours.BLUE
                            + "MAT237Y. "
                            + ConsoleColours.RESET
                            + "Accepted Sessions are"
                            + " (F,S,Y)");
            String courseInput = scanner.nextLine();
            Matcher matcher = validInput.matcher(courseInput);
            if (matcher.find()) {
                courses.add(courseInput);
                a++;
            } else {
                System.out.print(ConsoleColours.RED);
                System.out.printf("Input of %s did not match expected format \n", courseInput);
                System.out.print(ConsoleColours.RESET);
            }
        }
        return courses;
    }

    /**
     * Prompts the user to specify the directory to a saved schedule. Serialization.
     *
     * @return a user-saved schedule that courses will be added to
     */
    public Schedule promptImportSchedule() {
        Scanner scanner = new Scanner(System.in);
        Schedule importedSchedule = new Schedule();
        boolean success = false;

        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Please enter the relative file path to the schedule you would like"
                        + " to import: ---"
                        + ConsoleColours.RESET);
        System.out.println("Current directory is: " + System.getProperty("user.dir") + ".");
        String directory = scanner.next();

        while (!success) {
            try {
                File file = new File(directory);
                Reader fileReader = new FileReader(file);
                importedSchedule = new ICSImporter().importSchedule(fileReader);
                fileReader.close();
                success = true;
            } catch (IOException exception) {
                System.out.print(ConsoleColours.RED);
                System.out.println("Invalid directory. Please try again.");
                System.out.print(ConsoleColours.RESET);
                directory = scanner.next();
            }
        }
        System.out.print(ConsoleColours.GREEN);
        System.out.println("Schedule read successfully:\n");
        System.out.print(ConsoleColours.RESET);
        System.out.println(importedSchedule);
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Which courses would you like to add to this schedule? ---"
                        + ConsoleColours.RESET);
        return importedSchedule;
    }

    /**
     * Asks the user which filters they would like to add during schedule generation.
     *
     * @param userCourses the courses the user will take
     * @return a list of filters for their schedules
     */
    public List<Filter> promptUserFilters(List<Course> userCourses) {
        List<Filter> userFilters = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Would you like to configure any criteria for your schedules? ---"
                        + ConsoleColours.RESET);
        System.out.println(
                " 1 - "
                        + ConsoleColours.BLUE
                        + "Time conflicts\n"
                        + ConsoleColours.RESET
                        + " 2 - "
                        + ConsoleColours.BLUE
                        + "Delivery method\n"
                        + ConsoleColours.RESET
                        + " 3 - "
                        + ConsoleColours.BLUE
                        + "Enforce a time gap between courses\n"
                        + ConsoleColours.RESET
                        + " 4 - "
                        + ConsoleColours.BLUE
                        + "Enforce times when you have no courses\n"
                        + ConsoleColours.RESET
                        + "Please enter your choices as valid integer inputs with spaces. (i.e. '1"
                        + " 2 3' or '2' or '').\n"
                        + "If you do not wish to configure any criteria, quit the selection.\n"
                        + "Press 'Q' to quit selection.");
        boolean[] filterCodes = new boolean[4];

        while (scanner.hasNextInt()) {
            int index = scanner.nextInt();
            try {
                filterCodes[index - 1] = true;
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.print(ConsoleColours.RED);
                System.out.println("Invalid integer selection. Please enter an valid option:");
                System.out.print(ConsoleColours.RESET);
            }
        }

        for (int i = 0; i < filterCodes.length; i++) {
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

    /** Confirms whether user wants to generate all schedules or use one by one generation. */
    public void selectGenerationMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Would you like to generate schedules one by one? ---\n"
                        + ConsoleColours.RESET
                        + "Your schedule will be populated with only one course at a time to allow"
                        + " for specific time slot selection.");
        PromptHelpers.promptYNSelection();

        while (scanner.hasNextInt()) {
            int input = scanner.nextInt();
            if (input == 1) {
                this.generationMode = GenerationMode.ONE_BY_ONE;
                return;
            }
            if (input == 0) {
                this.generationMode = GenerationMode.ALL_PERMUTATIONS;
                return;
            }
        }
    }

    /**
     * Asks user to select the next base schedule for permutation in Scheduler.
     *
     * @param userSchedules the schedules meeting previous user specifications with one more course
     *     being permuted
     * @return if the user selects a schedule around
     */
    public Schedule promptUserBaseSchedule(List<Schedule> userSchedules) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Please select the schedule around which you want other time slots"
                        + " to be populated. ---"
                        + ConsoleColours.RESET);
        Schedule nextSchedule = this.displayUserSchedules(userSchedules);

        if (nextSchedule == null) {
            System.out.println(
                    ConsoleColours.RED
                            + "You have not selected a schedule.\n"
                            + ConsoleColours.RESET
                            + "The scheduler will generate all available schedules meeting previous"
                            + " specifications.");
        }
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Would you like to continue one-by-one generation? ---"
                        + ConsoleColours.RESET);
        PromptHelpers.promptYNSelection();

        while (scanner.hasNextInt()) {
            int input = scanner.nextInt();
            if (input == 1) {
                this.generationMode = GenerationMode.ONE_BY_ONE;
                return nextSchedule;
            }
            if (input == 0) {
                this.generationMode = GenerationMode.ALL_PERMUTATIONS;
                return nextSchedule;
            }
        }
        return nextSchedule;
    }

    /**
     * Outputs schedules meeting user criteria. User can navigate through schedules and save them.
     *
     * @param userSchedules schedules meeting filter criteria
     *     <p>attribute 'generationMode' is used in this method with 0 -> returning a Schedule is
     *     not an option 1 -> returning a Schedule is an option Note: returning a schedule is
     *     required in 1 by 1 generation
     */
    public Schedule displayUserSchedules(List<Schedule> userSchedules) {
        Scanner scanner = new Scanner(System.in);
        int numOfSchedules = userSchedules.size() - 1;
        int scheduleNumber = 0;
        char userActivity = 'W';

        if (numOfSchedules == -1) {
            System.out.print(ConsoleColours.RED);
            System.out.println("No schedules meeting these criteria could be created.");
            System.out.print(ConsoleColours.RESET);
            return null;
        }

        while (userActivity != 'Q') {
            Schedule currSchedule = userSchedules.get(scheduleNumber);
            System.out.println(
                    ConsoleColours.BLUE_UNDERLINED
                            + "These are schedules meeting your criteria:"
                            + ConsoleColours.RESET);
            System.out.println(
                    "Schedule No. "
                            + ConsoleColours.WHITE_BOLD_BRIGHT
                            + (scheduleNumber + 1)
                            + " / "
                            + (numOfSchedules + 1)
                            + ConsoleColours.RESET
                            + ".");
            System.out.println();
            System.out.println(currSchedule);
            System.out.println();
            System.out.println(
                    " • Press 'Q' to" + ConsoleColours.RED + " quit." + ConsoleColours.RESET);
            System.out.println(
                    " • Press '>' to go to the"
                            + ConsoleColours.BLUE
                            + " next schedule."
                            + ConsoleColours.RESET);
            System.out.println(
                    " • Press '<' to go to the"
                            + ConsoleColours.BLUE
                            + " previous schedule."
                            + ConsoleColours.RESET);
            System.out.println(
                    " • Press 'S/C' to"
                            + ConsoleColours.BLUE
                            + " save this schedule as an .ics/.csv file."
                            + ConsoleColours.RESET);
            if (this.generationMode == GenerationMode.ONE_BY_ONE) {
                System.out.println(
                        " • Press 'X' to"
                                + ConsoleColours.BLUE
                                + "build courses around this schedule"
                                + ConsoleColours.RESET);
            }

            char userInput = scanner.next().charAt(0);
            switch (userInput) {
                case 'Q':
                    userActivity = 'Q';
                    break;
                case '<':
                    if (scheduleNumber > 0) {
                        scheduleNumber--;
                    } else {
                        System.out.print(ConsoleColours.RED);
                        System.out.println("No schedule before this one.");
                        System.out.print(ConsoleColours.RESET);
                    }
                    break;
                case '>':
                    if (scheduleNumber < numOfSchedules) {
                        scheduleNumber++;
                    } else {
                        System.out.print(ConsoleColours.RED);
                        System.out.println("No schedules after this one.");
                        System.out.print(ConsoleColours.RESET);
                    }
                    break;
                case 'S':
                    System.out.print(ConsoleColours.GREEN);
                    System.out.println("Saving this schedule in .ics format...");
                    System.out.print(ConsoleColours.RESET);
                    new ICSExporter().outputSchedule(currSchedule);
                    break;
                case 'C':
                    System.out.print(ConsoleColours.GREEN);
                    System.out.println("Saving this schedule in .csv format...");
                    System.out.print(ConsoleColours.RESET);
                    Exporter exporter = new CSVExporter();
                    exporter.outputSchedule(currSchedule);
                    break;
                case 'X':
                    if (this.generationMode == GenerationMode.ONE_BY_ONE) {
                        return currSchedule;
                    }
                    break;
            }
        }
        return null;
    }

    /**
     * PRIVATE METHODS BELOW
     *
     * <p>These guys are used to simplify I/O methods above.
     */

    /**
     * Handles I/O request for filter that includes or excludes time conflicts.
     *
     * @return list of filters meeting user specification
     */
    private static List<Filter> promptTimeConflictFilter() {
        Scanner scanner = new Scanner(System.in);
        List<Filter> newFilters = new ArrayList<>();
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Would you like to allow time conflicts between your courses? ---"
                        + ConsoleColours.RESET);
        PromptHelpers.promptYNSelection();

        while (scanner.hasNextInt()) {
            int input = scanner.nextInt();
            if (input == 0) {
                newFilters.add(new ConflictFilter());
                System.out.print("Schedules with time conflicts will be ");
                System.out.print(ConsoleColours.RED_BOLD);
                System.out.print("REMOVED.\n");
                System.out.print(ConsoleColours.RESET);
                return newFilters;
            } else if (input == 1) {
                System.out.print("Schedules with time conflicts will be ");
                System.out.print(ConsoleColours.GREEN_BOLD);
                System.out.print("ALLOWED.\n");
                System.out.print(ConsoleColours.RESET);
                return newFilters;
            }
        }
        return newFilters;
    }

    /**
     * Handles I/O request for filter that includes or excludes in-person courses.
     *
     * @return list of filters meeting user specification
     */
    private static List<Filter> promptInPersonFilter() {
        Scanner scanner = new Scanner(System.in);
        List<Filter> newFilters = new ArrayList<>();
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Would you like all courses IN-PERSON or ONLINE? ---"
                        + ConsoleColours.RESET);
        PromptHelpers.promptGeneralSelection("IN-PERSON", "ONLINE");

        while (scanner.hasNextInt()) {
            int input = scanner.nextInt();
            if (input == 1) {
                newFilters.add(new InPersonFilter(true));
                System.out.print("Schedules with ONLINE courses will be ");
                System.out.print(ConsoleColours.RED_BOLD);
                System.out.print("REMOVED.\n");
                System.out.print(ConsoleColours.RESET);
                return newFilters;
            } else if (input == 0) {
                newFilters.add(new InPersonFilter(false));
                System.out.print("Schedules with IN-PERSON will be ");
                System.out.print(ConsoleColours.RED_BOLD);
                System.out.print("REMOVED.\n");
                System.out.print(ConsoleColours.RESET);
                return newFilters;
            }
        }
        return newFilters;
    }

    /**
     * Handles I/O request for filter that includes or excludes in-person courses.
     *
     * @return list of filters meeting user specification
     */
    private static List<Filter> promptSpaceFilter() {
        Scanner scanner = new Scanner(System.in);
        List<Filter> newFilters = new ArrayList<>();
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Would you like to enforce a time gap between all courses? ---\n"
                        + ConsoleColours.RESET
                        + "Enter an integer for the number of hours in the time gap. \n"
                        + "Press 'Q' to quit selection.");

        if (scanner.hasNextInt()) {
            int gap = scanner.nextInt();
            newFilters.add(new SpaceFilter(gap));
            System.out.println(
                    "ONLY schedules with " + gap + " hour gap between classes will be generated.");
            return newFilters;
        }
        System.out.println(
                ConsoleColours.RED
                        + "You did not specify a time gap. Quitting selection."
                        + ConsoleColours.RESET);
        return newFilters;
    }

    /**
     * Handles I/O request for filter that schedules courses during specific times.
     *
     * @return list of filters meeting user specification
     */
    private static List<Filter> promptTimeFilter() {
        Scanner scanner = new Scanner(System.in);
        List<Filter> newFilters = new ArrayList<>();

        Day[] days = {
            Day.ALL_DAYS, Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY
        };
        String[] dayStrings = {
            "Everyday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
        };

        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT
                        + "--- Would you like to specify times during which you have courses? ---"
                        + ConsoleColours.RESET);
        PromptHelpers.promptYNSelection();

        int input = 0;
        if (scanner.hasNextInt()) {
            input = scanner.nextInt();
        }

        while (input == 1) {
            System.out.println(
                    ConsoleColours.WHITE_BOLD_BRIGHT
                            + "--- During which day do you want to include times? ---\n"
                            + ConsoleColours.RESET
                            + "Press 0 - "
                            + ConsoleColours.BLUE
                            + "EVERYDAY\n"
                            + ConsoleColours.RESET
                            + "Press 1 - "
                            + ConsoleColours.BLUE
                            + "MONDAY\n"
                            + ConsoleColours.RESET
                            + "Press 2 - "
                            + ConsoleColours.BLUE
                            + "TUESDAY\n"
                            + ConsoleColours.RESET
                            + "Press 3 - "
                            + ConsoleColours.BLUE
                            + "WEDNESDAY\n"
                            + ConsoleColours.RESET
                            + "Press 4 - "
                            + ConsoleColours.BLUE
                            + "THURSDAY\n"
                            + ConsoleColours.RESET
                            + "Press 5 - "
                            + ConsoleColours.BLUE
                            + "FRIDAY"
                            + ConsoleColours.RESET);

            int day = scanner.nextInt();
            while (!(day >= 0 && day <= 5)) {
                System.out.println(
                        ConsoleColours.RED
                                + "Invalid input. Please enter another integer."
                                + ConsoleColours.RESET);
                day = scanner.nextInt();
            }

            System.out.println(
                    ConsoleColours.WHITE_BOLD_BRIGHT
                            + "--- From what time during these day(s) do you want classes? ---"
                            + ConsoleColours.RESET);
            LocalTime startTime = CommandLineInterface.timeInputHandler();
            System.out.println(
                    ConsoleColours.WHITE_BOLD_BRIGHT
                            + "--- Until what time during these day(s) do you want classes? ---"
                            + ConsoleColours.RESET);
            LocalTime endTime = CommandLineInterface.timeInputHandler();

            if (startTime.compareTo(endTime) > 0) {
                System.out.println(
                        ConsoleColours.RED
                                + "Your start time is before your end time."
                                + " Please try again during the next iteration."
                                + ConsoleColours.RESET);
            } else {
                // WHY DOES DAY NOT HAVE TO STRING METHOD???? quick fix for now by hardcoding an
                // array
                System.out.println(
                        ConsoleColours.WHITE_BOLD_BRIGHT
                                + "--- Confirmation ---"
                                + ConsoleColours.RESET);
                System.out.println(
                        "You would like classes during "
                                + dayStrings[day]
                                + " from "
                                + startTime.toString()
                                + " until "
                                + endTime.toString());
                System.out.println("Is the above correct?");
                PromptHelpers.promptYNSelection();
                int sc = scanner.nextInt();
                if (sc == 1) {
                    newFilters.add(new TimeFilter(startTime, endTime, days[day]));
                } else if (sc == 0) {
                    System.out.print(ConsoleColours.RED);
                    System.out.println("Please try again on the next iteration.");
                    System.out.print(ConsoleColours.RESET);
                } else {
                    System.out.print(ConsoleColours.RED);
                    System.out.println("Invalid input. Please try again on the next iteration.");
                    System.out.print(ConsoleColours.RESET);
                }
            }
            System.out.println(
                    ConsoleColours.WHITE_BOLD_BRIGHT
                            + "--- Would you like to restrict your schedule to another block of"
                            + " time? ---"
                            + ConsoleColours.RESET);
            PromptHelpers.promptYNSelection();
            System.out.println(
                    "Quitting the selection means blocks will NOT be added to scheduling.");

            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input == 1) {
                    System.out.println(ConsoleColours.GREEN + "Looping..." + ConsoleColours.RESET);
                } else if (input == 0) {
                    System.out.print(ConsoleColours.GREEN);
                    System.out.println(
                            "Your selected blocks of time are saved and courses taking place"
                                    + " outside these times will be excluded. Exiting selection.");
                    System.out.println(ConsoleColours.RESET);
                    return newFilters;
                } else {
                    System.out.print(ConsoleColours.GREEN);
                    System.out.println(
                            "Your selected times will not be included in schedule generation."
                                    + " Exiting selection.");
                    System.out.println(ConsoleColours.RESET);
                    return new ArrayList<>();
                }
            } else {
                System.out.print(ConsoleColours.RED);
                System.out.println(
                        "Your selected times will not be included in schedule generation. Exiting"
                                + " selection.");
                System.out.println(ConsoleColours.RESET);
                return new ArrayList<>();
            }
        }
        if (input == 0) {
            System.out.print(ConsoleColours.RED);
            System.out.println("Input not selected.");
            System.out.print(ConsoleColours.RESET);
        }
        return new ArrayList<>();
    }

    /**
     * Handles I/O for time formatting for the TimeFilter method.
     *
     * @return LocalTime object that represents a time
     */
    private static LocalTime timeInputHandler() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please use HH:MM format in 24 hour time.");
        while (true) {
            String[] input = scanner.next().split(":| +");
            try {
                int hours = Integer.parseInt(input[0]);
                int mins = Integer.parseInt(input[1]);
                return LocalTime.of(hours, mins, 0, 0);
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.print(ConsoleColours.RED);
                System.out.println("Invalid input. PLEASE use HH:MM format in 24 HOUR TIME:");
                System.out.print(ConsoleColours.RESET);
            }
        }
    }

    public enum GenerationMode {
        ALL_PERMUTATIONS(0),
        ONE_BY_ONE(1);

        private final int generationMode;

        GenerationMode(int i) {
            this.generationMode = i;
        }

        public int getDay() {
            return generationMode;
        }
    }
}
