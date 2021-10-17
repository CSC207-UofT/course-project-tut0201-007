## Overview
The project domain of our group is a Scheduling App that allows Students to specify courses they'd like to take, as well as criteria for their final schedule. Currently there are some course scheduling applications out there, but we felt that they were:

- Far too large and clunky, requiring numerous installs and setup
- Did not use the Timetable API
- Not enough customization
- Incorrect schedule generation
- Not accessible to people outside CS

Which is why we decided to make our own. 

## The Project
A user specifies which courses they want to take, and also specify filters, like "No classes after 5 PM" through the CLI. The program queries the U of T Academic Calendar for the Tutorial & Lecture Sections of each requested course, and creates all schedules that meet the given criteria. This can be navigated either directly in the CLI. The user selects a generated schedule, and the program generates ICS invites that can be used with most calendar apps.

### Use Cases:

#### CourseCreator ->
Creates a course object, populated with information from the API information retrieved through APIWorker.

#### Scheduler ->
Creates a schedule object, populated with permutations from the course objects.

#### ICSCreator ->
Export schedule as a .ics file, that can be interpreted by the vast majority of calendar apps.

### CLI Commands:


### Potential Additions for Future Phases:

Improved Parameters ->
* Use Distance between class sessions to support Filters like "Walking distance must be less than 10 minutes between sessions".

Optimization ->
* Use multithreading to improve the time taken to genererate schedule permutations
* Use caching in the cli + APIWorker to prevent repetitive API calls

Comments:
- Details about the entire app (as much as possible)
- Use cases (CLI commands, example, expected behaviour)
- "Granular"
- Features + functionality
