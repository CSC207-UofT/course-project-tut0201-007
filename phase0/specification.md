## Overview
The project domain of our group is a Scheduling App that allows Students to specify courses they'd like to take, as well as criteria for their final schedule. Currently there are some course scheduling applications out there, but we felt that they were:

- Far too large and clunky, requiring numerous installs and setup
- Did not use the Timetable API
- Not enough customization
- Incorrect schedule generation
- Not accessible to people outside CS

Which is why we decided to make our own. 

## The Project
A user specifies which courses they want to take, and also specify filters, like "No classes after 5 PM", or to find sections with no conflicts, through the CLI. The program queries the U of T Academic Calendar for the Tutorial & Lecture Sections of each requested course, and creates all schedules that meet the given criteria. This can be navigated either directly in the CLI. The user selects a generated schedule, and the program generates ICS invites that can be used with most calendar apps.

### Entities:

#### entities.Schedule ->
A class that represents a possible schedule consisting of distinct lecture and tutorial sessions for each course.

#### entities.Course ->
A class that stores possible lecture and tutorial sections for each course.

### entities.Session ->
A class that represents a distinct time slot for some class. It is used by course, and stored in schedule.

### Use Cases:

#### workers.CourseCreator ->
Creates a course object, populated with information from the API information retrieved through workers.APIWorker.

#### workers.Scheduler ->
Creates permutations of all possible schedules, and then passes them through filter classes that remove schedules.

#### ICSCreator ->
Export schedule as a .ics file, that can be interpreted by the vast majority of calendar apps.

#### filters.Filter Subclasses ->
Classes that verify schedules based on user requested specifications, i.e. distances.



### CLI Commands/Controller class:

#### controllers.CommandLineInterface ->
Main class for the project, prompts user to input each of their classes, then uses the workers.Scheduler class to create a schedule and outputs details about each course as well as a basic schedule.

### Potential Additions for Future Phases:

entities.Schedule Generation ->
* Recursively creating schedules, ensuring that there are no overlapping courses. This is the main feature of the project, and the groundwork for it has been laid in phase 0

Improved Parameters ->
* Use Distance between class sessions to support Filters like "Walking distance must be less than 10 minutes between sessions".

Optimization ->
* Use multithreading to improve the time taken to genererate schedule permutations
* Use caching in the cli + workers.APIWorker to prevent repetitive API calls

Comments:
- Details about the entire app (as much as possible)
- Use cases (CLI commands, example, expected behaviour)
- "Granular"
- Features + functionality
