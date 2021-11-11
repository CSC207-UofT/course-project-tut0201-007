# Specification Summary
Our project domain is a Scheduling App for U of T courses which lets you filter schedules by your criteria.

The project works by taking User Input regarding courses they want, and filters for the schedule through the CLI. We use the U of T Academic Calendar for the Tutorial & Lecture sections for each requested course, and generate Schedules that meet the criteria. The User selects a generated schedule through the CLI, and then generates .ics invites for the sections.

# CRC Model Summary
* Controller:
  * controllers.CommandLineInterface, maps user input to calling commands
* Entities:
  * entities.Schedule: Stores Lectures & Tutorials in schedule, and can add either Lecture or Tutorial
  * entities.Course: Stores Lectures, Sessions, courseID for a course
  * entities.Session: Represents Lecture or Tutorial with location and Date & Time
* Frameworks & Drivers:
  * workers.APIWorker: Makes API request to U of T API, parses returned JSON payload and stores
* Use Cases:
  * filters.Filter: Interface for a filter, like "No courses after 4 PM"
  * TimeFilter: Filters for courses only being within a certain time range
  * workers.CourseCreator: Populates new entities.Course Object using workers.APIWorker
  * workers.Scheduler: Generates Schedules using filters
  * IcsCreator: Create ICS file for a entities.Session
# Scenario Walk-Through Summary
The program takes User Input through the CLI. It takes the number of courses the User wants to take, and the entities.Course Codes for courses the user wants to take. It parses the courses with **controllers.CommandLineInterface**, and creates a **entities.Schedule** containing the desired courses with **workers.Scheduler**, and outputs the **entities.Schedule** to the CLI. **workers.Scheduler** uses **workers.CourseCreator** to instantiate a **entities.Course** for each course, and outputs the courses to the CLI. **workers.CourseCreator** uses info from **workers.APIWorker**, which queries the U of T Academic Calendar API for information for a single course.

For our walk-through, we used the example of wanting to input 2 courses, and wanting to take CSC258 and MAT237.

# Open Questions
* Is there a way we can use our Filters to make the schedule generation more efficient? Right now we plan to get all permutations, and check if they meet our filters.Filter's specifications after.
* What would be the best way to structure our entities.Session and entities.Course objects to allow for useful user specifications?

# What's Worked Well so Far
* The GitHub Projects tool has been especially helpful in organizing tasks and distributing the workload so that everyone is on the same page. It has also been a good place to plan future ideas for the project that can be implemented in later phases.
* The University of Toronto API has worked well. The API provides a lot of information about courses, which makes the schedule creator program's ability to generate schedules based on user specification far more powerful and flexible.
# Group Member Work Overview


## Anton
* Worked On:
  * workers.Scheduler, entities.Schedule, entities.Session 
  * Progress Report
* To Work on:
  * Making workers.Scheduler generate all permutations.
  * Creating different filters, e.g. timeslot restrictions, conflicts, physical distance etc.
  * Grouping sessions for distinct lecture sections for courses.
## Kenneth
* Worked On:
  * CRC Cards
  * Walkthrough
  * Refactoring
  * Progress Report
* To Work on:
  * Create UseCase for converting entities.Session (ICSCreator)
  * Implement TimeFilter & other filters
## Rory
* Worked On:
  * entities.Course, entities.Session, workers.CourseCreator
  * Progress Report
* To Work on:
  * Improving workers.CourseCreator and entities.Course classes to allow for greater user specification
  * Redesigning entities.Session as an abstract class, and making SyncSession and AsyncSession
  * Creating different user specifications, e.g. timeslot restrictions, conflicts, physical distance etc.
## Siddarth
* Worked On:
  * workers.APIWorker
  * Specification, Progress Report
* To Work on:
  * Improving run time for permutation generation
  * Creating different filters, e.g. timeslot restrictions, conflicts, physical distance etc. 
## Lorena 
* Worked On:
  * CRC cards
  * entities.Schedule methods and tests 
  * Progress Report 
  * Refactoring
* To Work On:
  * Making `addLecture` and `addTutorial` methods more efficient 
  * Implementing methods for schedule generation based on filters 
## Baker
* Worked On:
  * Progress report
  * controllers.CommandLineInterface class
* To Work On
  * Implementing effective scheduling and possibly multithreading
  * Making CLI more interactive, with the ability to upload txt file of classes
