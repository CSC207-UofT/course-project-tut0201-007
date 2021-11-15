# Design Document

## Updated Specification

`TODO: This is the Phase 0 specification, needs to be updated.`

### Overview
The project domain of our group is a Scheduling App that allows Students to specify courses they'd like to take, as well as criteria for their final schedule. Currently there are some course scheduling applications out there, but we felt that they were:

- Far too large and clunky, requiring numerous installs and setup
- Did not use the Timetable API
- Not enough customization
- Incorrect schedule generation
- Not accessible to people outside CS

Which is why we decided to make our own. 

### The Project
A user specifies which courses they want to take, and also specify filters, like "No classes after 5 PM", or to find sections with no conflicts, through the CLI. The program queries the U of T Academic Calendar for the Tutorial & Lecture Sections of each requested course, and creates all schedules that meet the given criteria. This can be navigated either directly in the CLI. The user selects a generated schedule, and the program generates ICS invites that can be used with most calendar apps.

### Entities:

#### Schedule 
A class that represents a possible schedule consisting of distinct lecture and tutorial sessions for each course.

#### Course 
A class that stores possible lecture and tutorial sections for each course.

#### Section 
A class that represents a distinct time slot (a single lecture/tutorial) for some class. It is used by course, and stored in schedule.

#### Timeslot 
A class that stores the time, day, and location of a lecture or tutorial. 

### Use Cases:

#### CourseCreator 
Creates a Course object, populated with information from the API information retrieved through APIWorker.

#### Scheduler 
Creates permutations of all possible schedules, and then passes them through Filter classes that remove schedules.

#### ScheduleImporter 
Parses information written in an ICS file and converts it into a Schedule object.

#### ScheduleExporter 
Export schedule as a .ics file, that can be interpreted by the vast majority of calendar apps.

#### Filter Subclasses 
Classes that verify schedules based on user requested specifications, i.e. distances.

### CLI Commands/Controller class:

#### Controller 

#### CommandLineInterface 
Main class for the project, prompts user to input each of their classes, then uses the Scheduler class to create a schedule and outputs details about each course as well as a basic schedule.

### Potential Additions for Future Phases:

Schedule Generation 
* Recursively creating schedules, ensuring that there are no overlapping courses. This is the main feature of the project, and the groundwork for it has been laid in phase 0

Improved Parameters 
* Use Distance between class sessions to support Filters like "Walking distance must be less than 10 minutes between sessions".

Optimization 
* Use multithreading to improve the time taken to generate schedule permutations
* Use caching in the cli + APIWorker to prevent repetitive API calls

Comments:
- Details about the entire app (as much as possible)
- Use cases (CLI commands, example, expected behaviour)
- "Granular"
- Features + functionality

## UML Diagram

![UML](UML.png?raw=true "UML Diagram")

## Major Design Decisons

### Changes to Session
`Session` was originally intended to represent a time during which a particular lecture or tutorial would occur. However, as we worked on the project, we found that it would be much more convenient to group the different times a lecture would meet, rather than represent them seperately. We considered multiple implementations to account for multiple lecture sections. Our first idea was to store `Session` entites in a map from section ID to an ArrayList of `Session` classes, but we instead elected to go with a solution that better followed the structure of the rest of our project, and avoided too much object coupling. We decided to make `Session` a class that stored lower-level entities, and made a new entity named `Timeslot` in order to represent the various times. Multiple `Timeslot` objects are stored in `Session`. `Session` was also renamed to `Section`, simply because the changes we made more closely resembled what U of T and it's students call lecture and tutorial sections.

### Controller

Our CommandLineInterface class initially violated the single responsibility principle since it took on too many tasks; it would take user input, call Scheduler, and handles output. As well, we noticed that our Scheduler class was responsible for the instantiation of course objects. It became evident that our Controler functionality was split between the CommandLineInterface and Scheduler classes. In order to adhere to the single responsibility and open/closed principles of software design, we created the Controller class. This class now holds the main method, calls the CommandLineInterface to prompt for user input, and then instantiates Course objects for Scheduler. Scheduler was changed so that it accepts only Course objects as paramaters to avoid instantiation of Courses from String course ID within scheduling methods. Overall, this will allow for greater flexibility with extension of our program's UI, control flow, and output.

### Data Serialization
For our data serialization functionality, we decided to use ICS files for our Data serialization because ICS files are the standard for storing online calendars. Since we use ICS files to store our own schedules, that means that we can directly import schedules from Google Calendar, or other scheduling apps, and use them to apply filters to them to create new schedules. We created two classes, one for importing schedules from ICS files (**ScheduleImporter**) and one for exporting schedules (**ScheduleExporter**) to ICS files.

We chose to use ICS files over a database because we don't expect to be storing much information. Under our specification, we expect that users will, at most, import a few schedules that they generated earlier, and that the users will not save that many final schedules. Also, since we only need to serialize our data when importing or exporting schedules, both of which happen infrequently, reduced speed from not using a database is trivial 

## Clean Architecture

`TODO: Describe a scenario walk-through and highlight how it demonstrates Clean Architecture`

### **User I/O**: CommandLineInterface, Calendar exporting 
The user interacts with out CLI input class. At the moment, the CLI class and controllers class are closely related. The user gives input to the CommandLineInterface which is used to control scheduling in the Main method. These two classes are the same since there is high cohesion between the user input and the data the controllers works with.

The calendar export class creates .ics files that contain times for a specific schedule. The user can also choose which generated schedule to export to an .ics file through the CLI. In addition, the user can also specify ICS files to import into the program via the CLI.

(**Note for ourselves** We need to implement the input class to view/swtich between schedules, and save specific schedules in .ics format. For this we need a more sophisticated terminal. We will also need to separate our controllers and input if this happens in order to follow clean architecture. Please review the use of PicoCLI, or consider a GUI.)

### **Controller**: CommandLineInterface

The main method of our program lies in the CommandLineInterface class. User input is collected, and calls to Scheduler are made based on this input. Scheduler is a use case class that calls on other use case and entity classes, subsequently returning schedules meeting user criteria to the controllers.

### **Use Case**: Scheduler, CourseGenerator, APIWorker

Scheduler takes courses and criteria specified by the user, generates all course schedules satisfying this criteria, then returns them. Scheduler calls Filter classes in order to filter courses not satisfying some criterion. Scheduling occurs with the strategy design pattern so that if the user has a certain priority for a course, schedules are generated that prioritize each course.

CourseGenerator is called by Scheduler/(**our future controllers**) and instantiates Course objects representing the user's courses. It does so by calling APIWorker to retrieve data from the U of T API. The CourseGenerator then creates Sessions and TimeSlots (ok this seems like it's doing too much), adding these Objects to the list of Sessions each course has.

APIWorker takes course codes and gets their information from the U of T API. This allows CourseCreator to create representations of the courses that is useful to our software.

(**Note for ourselves** We REALLY need to consider how we actually implement this. There are a few obvious questions:

1. Making all permutations is extremely inefficient. We need to come up with a way to check filters for schedules while they are being generated.
2. We should make 'ScheduleGenerator' or something for the specific implementation.
3. How does the user 'pass in' filters? We should consider how we implement the controllers and encode user input instead of using text.
4. How do we ensure user priority for schedules are satisfied during generation? How do we make sure we return the least amount of 'useless' schedules? We need to make a decision about what is truly 'useless' and not consider those cases.
5. We should consider making the collection of courses passed into schedule cleaner. Course object instantiation should occur outside of scheduler, or else scheduler has too many responsibilities.
6. What is the **single responsibility** of scheduler? What is the single responsibility of each of our classes? Honestly not many of ours follow the S principle.
7. Are we blurring the lines between 'scheduler' and our controllers? We should create a distinct controllers class. 
)

### **Entity**: Schedule, Course, Session, Timeslot

Schedule represents a particular scheduler, with specific lecture and tutorial sessions for a course. It is manipulated by the above use case classes, notably Scheduler, and its representation is eventually returned by the controllers to the user.

Course represents a particular course with various sessions. The sessions of courses are added to different schedules during schedule generation. Filters check the timeslots of sessions to satisfy certain criteria.

Sessions represent a collection of time slots with a room and code. These are taken from their respective courses and stored in Schedule during schedule generation.

(**Note for ourselves** Comparisons of sessions should be clean. Again, we need to consider the implementation since it seems there is a lot of unnecessary intertwining of classes. We should also encapsulate the return of times in the session so we do not need to get the list of slots and do operations or something.)

### Filter & implementations

Different Filter objects are instantiated based on the criteria a user provides for their scheduler. The filters are called during schedule generation in order to verify whether a particular schedule meets a user criterion. It main purpose is to check a schedule and return true/false.

## SOLID Design Principles

`TODO: Include parts of the code that DON'T adhere to SOLID, and talk about how it can be fixed`

### Single-responsibility principle

The workers package exemplifies the use of the Single-responsibility principle in our program. Many of our entity classes are comprised of a collection of other entity classes (i.e. a `Section` is a collection of `Timeslot` classes). We wanted to avoid having an entity class be responsible for both representing itself and for constructing itself from other entities, as this would violate the SRP. To solve this problem we created classes that have the sole responsibility of creating these entities. For example, instead of creating a `Course` object by instantiating a course with a collection of `Section` classes, the `CourseCreator` class bears this responsibility. 

### Open-closed principle

The Open-closed principle is demonstrated by the `Filter` interface. Along with choosing classes to be scheduled, the user is given the option to filter out unwanted schedules based on certain criteria. If the user wanted to have a schedule with no class on a Friday, the `ExcludeTimeFilter` class is used, which implements `Filter`. Because `Filter` is an interface, it is open for extension when creating a new kind of filter, but closed for modification.

### Liskov substitution principle

With our `Filter` interface, any usages of its implementing classes can be replaced with each other without changing functionality since they all implement the same interface methods. Other than this, we don't have many usages of inheritance in our program. The Liskov Substitution Principle isn't demonstrated very strongly as a result. In order to strengthen how we demonstrate this principle, we can design a superclass in the future that can be replaced by its subclasses without altering the functionality of the program. 

### Interface segregation principle

The Interface Segregation principle is demonstrated by the `Filter` interface and all of the filter classes that implement this interface. All of the fiters implement the `checkSchedule()` interface method which takes in a schedule object. All of our filter classes need to extract relevant information from a given schedule to see if it meets the criteria or not. There are no cases where a filter does not implement this method, hence satisfying the Interface Segregation Principle. 

### Dependency inversion principle

## Packaging Strategies

We packaged our code using the packaging by layers strategy. This way we organized each clean architecture component into its 
own package, such as controllers, entities, filters, and Controllers contains our command line interface, entities
contains all objects (`Course`, `Schedule`, `Session`), filters contains all implementations of the Filter interface, and workers 
contains all of our use cases. 

Since we don't have many classes for each layer, the code is well organized and simple to navigate through. We 
put filters into its own package because we have a filter interface and its subclasses which should be grouped together. 

Expansion of the program will be easy, as we can add each new clean architecture component into its associated package.

## Design Pattern Summary

`We can add patterns that we didn't use but should have, or are planning to implement in the future`

### Strategy
This is design pattern is best exemplified by the "Filter" interface and it's subsequent implementations. The classes that implement it are:
- [InPersonFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/InPersonFilter.java)
- [SpaceFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/SpaceFilter.java)
- [CourseExclusionFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/CourseExclusionFilter.java)
- [ConflictFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/ConflictFilter.java)
- [TimeFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/TimeFilter.java)

The Strategy Design Pattern is a collection of encapsulated algorithm, that can be slotted in and out with one another. This lets the user use whichever strategy they would like. In order to do so the core abstraction is implemented by some interface, and classes that use this carry the specific implementations. The "core abstraction" is our `Filter` interface, that uses the method `checkSchedule` which is overrided and implemented differently in all classes that implement  `Filter`. Then, the user can use the UI outlined by `CommandLineInterface` to select which ones they would like to apply to their schedules.

### Decorator
For the filters

### Command
CLI

### Chain of Responsibility
CourseCreator

## Progress Report

### Open questions
- Can we further optimize our schedule generation, by using filters within the recursive method rather than applying them after all schedules have been generated? Would this even be more efficient?
- How do we improve the worst case runtime of our filters?

### What has worked well so far
- Linking Github Issues with Projects has a great automated feature where cue cards are automatically linked with PR's where issues are cited, and automatically get moved to the column they should be in.
- Pull Request reviews have been an efficient and concise way to communicate each group member's thoughts on design decisions, code formatting, and any other miscellaneous questions about the commits.

### Group member contributions & plans

#### Rory
* Worked On:
  * refactoring the CourseCreator and Section classes
  * InPersonFilter class
  * mock API for testing classes
  * design document
* To Work on:
  * implementing various other Filter subclasses
  * improved CLI schedule representation

