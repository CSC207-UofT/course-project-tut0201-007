# Design Document

## Updated Specification

### Overview
The project domain of our group is a Scheduling App that allows Students to specify courses they'd like to take, as well as criteria for their final schedule. Currently there are some course scheduling applications out there, but we felt that they were:

- Far too large and clunky, requiring numerous installs and setup
- Did not use the Timetable API
- Not enough customization
- Generated schedules incorrectly using outdated information

Which is why we decided to make our own.

### The Project
A user specifies which courses they want to take, and also specify filters, like "No classes after 5 PM", or to find sections with no conflicts, through the CLI. The program queries the U of T Academic Calendar for the Tutorial & Lecture Sections of each requested course, and creates all schedules that meet the given criteria. This can be navigated either directly in the CLI. The user selects a generated schedule, and the program generates .ics/.csv/.jpg files for different purposes, such as for calendar apps.

### Entities:

#### Schedule
A class that represents a particular schedule, with specific sections for a course. It is manipulated by the above use case classes, notably Scheduler, and its representation is eventually returned by Controller to the user.

#### Course
Course represents a particular course with various sections. The sections of courses are added to different schedules during schedule generation. Filters check the timeslots of sessions to satisfy certain criteria.

#### Section
A class that represents a distinct time slot (a single lecture/tutorial) for some class. It is used by course, and stored in schedule.

#### Timeslot
A class that stores the time, day, and location of a lecture or tutorial.

#### ExecutionState
An entity responsible for representing the execution state of the program. It stores user courses, next course being generated, and user specified base schedule.

### Use Cases:

#### CourseCreator
CourseCreator is called by Scheduler/Controller and instantiates Course objects representing the user's courses. It does so by using APIWorker to retrieve data from the U of T API. CourseCreator then creates Sections and Timeslots, adding these entities to a Course.

#### Scheduler
This class takes courses and criteria specified by the user, generates all course schedules satisfying this criteria, then returns them. Scheduler calls Filter classes in order to filter courses not satisfying some criterion. Scheduling occurs with the strategy design pattern so that if the user has a certain priority for a course, schedules are generated that prioritize each course.

#### APIWorker
APIWorker takes course codes and gets their information from the U of T API. This allows CourseCreator to create representations of the courses that is useful to our software.

#### Importer
An interface that outlines methods and parameters of any importing use-case class.

#### ICSImporter/CSVImporter
Parses information written in an .ics/.csv file and converts it into a Schedule object, which can them be modified by the user.

#### Exporter
An abstract class that contains methods shared across exporting classes.

#### ICSExporter/CSVExporter/ImageExporter
Export a schedule as a .ics/.csv/.jpg file, depending on what the user wants to get out of their schedule.

#### Filter Subclasses (ConflictFilter/CourseExclusionFilter/ExcludeTimeFilter/InPersonFilter/SpaceFilter/TimeFilter)
These classes are instantiated based on the criteria a user provides for their scheduler. Filter subclasses are called during schedule generation in order to verify whether a particular schedule meets a user criterion. It main purpose is to check a schedule and return true/false. 

### CLI Commands/Controller class:

#### Controller
The main method of our program lies in this class. This manages the CommandLineInterface, instantiates courses, added filters, and negotiates output.

#### CommandLineInterface
The UI of the program. Prompts user to input each of their classes/filters, then provides an appropriate schedule that may be exported as an .ics/.csv/.jpg.

## UML Diagram
![image](https://user-images.githubusercontent.com/22108651/144903389-b0de8406-6564-41c8-8c42-7f648018c21a.png)


## Major Design Decisons

### Changes to Session
`Session` was originally intended to represent a time during which a particular lecture or tutorial would occur. However, as we worked on the project, we found that it would be much more convenient to group the different times a lecture would meet, rather than represent them seperately. We considered multiple implementations to account for multiple lecture sections. Our first idea was to store `Session` entites in a map from section ID to an ArrayList of `Session` classes, but we instead elected to go with a solution that better followed the structure of the rest of our project, and avoided too much object coupling. We decided to make `Session` a class that stored lower-level entities, and made a new entity named `Timeslot` in order to represent the various times. Multiple `Timeslot` objects are stored in `Session`. `Session` was also renamed to `Section`, simply because the changes we made more closely resembled what U of T and it's students call lecture and tutorial sections.

### Controller

Our CommandLineInterface class initially violated the single responsibility principle since it took on too many tasks; it would take user input, call Scheduler, and handle outputs. As well, we noticed that our Scheduler class was responsible for the instantiation of course objects. It became evident that our Controller functionality was split between the CommandLineInterface and Scheduler classes. In order to adhere to the single responsibility and open/closed principles of software design, we created the Controller class. This class now holds the main method, calls the CommandLineInterface to prompt for user I/O, and then instantiates Course objects for Scheduler. Scheduler was changed so that it accepts Course objects as paramaters in scheduling methods to avoid instantiation of Courses from String course ID. Overall, this will allow for greater flexibility with extension of our program's UI, control flow, and output.

### Data Serialization
For our data serialization functionality, we decided to use ICS and CSV files for our Data serialization because ICS files are the standard for storing online calendars, and CSV files provide an alternative with spreadsheet functionality. Since we use ICS/CSV files to store our own schedules, that means that we can directly import schedules from Google Calendar, or other scheduling apps, and use them to apply filters to them to create new schedules. Since some parts of the import/export algorithm were similar for the different filetypes, we made an `Exporter` abstract class and an `Importer` interface. Then the classes `CSVExporter`, `CSVImporter`, `ICSExporter`, and `ICSImporter` are concrete implementations for this feature.

We chose to use ICS/CSV files over a database because we don't expect to be storing much information. Under our specification, we expect that users will, at most, import a few schedules that they generated earlier, and that the users will not save that many final schedules. Also, since we only need to serialize our data when importing or exporting schedules, both of which happen infrequently, reduced speed from not using a database is trivial. We also included CSV as a serialization option in order to support users who wanted to be able to manipulate their schedules in Excel.

### Scheduler Recursive Base Case
We had an elegant solution to generating schedules on top of a fixed set of courses. When a `Scheduler` object is instantiated, we set its instance attribute `schedule` to an empty schedule. Given a set of courses, this attribute is used as the base case for the recursive algorithm. The method `setBaseSchedule()` allows for this recursive base case to be changed. This allows for courses to be added to previously existing schedules, improves data serialization, and reduces redundant methods in `Scheduler`. For example, if a user imports a schedule with some preferable sections, this will be set as the recursive base case, and other schedules will be generated around these. This allows for greater flexibility in Phase 2 for selection, i.e. 'one by one' schedule generation.

## Clean Architecture

### Scenario walk-through

The program is run through the Controller class. Controller interacts with the CommandLineInterface class to prompt the user to enter a certain number of classes that they wish to schedule, and to apply any desired Filter classes. Controller then instantiates the given courses using the CourseCreator use-case class, which creates Course entities that are generated by APIWorker and consist of Section entities, which consist of Timeslot entities themselves. So far, clean architecture has been closely followed, as the program adheres to the Dependency Rule. Controller and CommandLineInput use CourseCreator and APIWorker, and CourseCreator and APIWorker use Course, Section, and Timeslot. The reverse isn't true, however, so the Dependency Rule is intact.

Following this, Controller generates Schedule entities using Scheduler. Controller then uses the ScheduleExporter use-case to generate an .ics file that provides data serialization for schedules that the user selects. Since the only data that passes between the layers of the program architecture are simple arguments, function calls, and maps, the Dependency Rule remains unbroken.

Alternatively, at the beginning of the CommandLineInterface, the user can choose to import an existing schedule from an .ics/.csv file for further modification. The steps the program takes to do this is essentially identical to that described above.

## SOLID Design Principles

### Single-responsibility principle

The workers package exemplifies the use of the Single-responsibility principle in our program. Many of our entity classes are comprised of a collection of other entity classes (i.e. a `Section` is a collection of `Timeslot` classes). We wanted to avoid having an entity class be responsible for both representing itself and for constructing itself from other entities, as this would violate the SRP. To solve this problem we created classes that have the sole responsibility of creating these entities. For example, instead of creating a `Course` object by instantiating a course with a collection of `Section` classes, the `CourseCreator` class bears this responsibility.

### Open-closed principle

The Open-closed principle is demonstrated by the `Filter` interface. Along with choosing classes to be scheduled, the user is given the option to filter out unwanted schedules based on certain criteria. If the user wanted to have a schedule with no class on a Friday, the `ExcludeTimeFilter` class is used, which implements `Filter`. Because `Filter` is an interface, it is open for extension when creating a new kind of filter, but closed for modification.

### Liskov substitution principle

With our `Filter` interface, any usages of its implementing classes can be replaced with each other without changing functionality since they all implement the same interface methods. Other than this, we don't have many usages of inheritance in our program. The Liskov Substitution Principle isn't demonstrated very strongly as a result. In order to strengthen how we demonstrate this principle, we can design a superclass in the future that can be replaced by its subclasses without altering the functionality of the program.

We also make use of the Liskov substitution principle to simplify importing pre-existing files, by using an `Importer` object, which is initialized to a `ICSImporter` or `CSVImporter`, both of which are it's subclasses, depending on what type of file we want to import. Since `Importer` is the superclass, it is substitutable by `ICSImporter` and `CSVImporter`. Implemented in [this pr](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/74).

### Interface segregation principle

The Interface Segregation principle is demonstrated by the `Filter` interface and all of the filter classes that implement this interface. All of the fiters implement the `checkSchedule()` interface method which takes in a schedule object. All of our filter classes need to extract relevant information from a given schedule to see if it meets the criteria or not. There are no cases where a filter does not implement this method, hence satisfying the Interface Segregation Principle.

### Dependency inversion principle
Our program applies the Dependency inversion principle to adhere to the Open-closed principle. In the example of OCP above, `Controller` acts a as a high-level module that depends on `Filter` as an abstraction layer. The actual `Filter` subclasses, such as `InPersonFilter` and `TimeFilter`, are implementations of the abstract layer. Therefore the 'details' of the program (i.e. what filters `Controller` calls and what each `Filter` subclass actually performs on the schedule) are dependent on the abstraction, and not the other way around.

## Packaging Strategies

We packaged our code using the packaging by layers strategy. This way we organized each clean architecture component into its own package, such as controllers, entities, filters, and Controllers contains our command line interface, entities
contains all objects (`Course`, `Schedule`, `Session`), filters contains all implementations of the Filter interface, and workers contains all of our use cases.

We also refactored the tests to follow our packaging strategy, as seen in
[this pull request.](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/59)

Since we don't have many classes for each layer, the code is well organized and simple to navigate through. We
put filters into its own package because we have a filter interface and its subclasses which should be grouped together.

Expansion of the program will be easy, as we can add each new clean architecture component into its associated package.

## Refactoring
* Refactored code to better utilize Liskov Substitution Principle by using an abstract class (`List`) rather than `ArrayList` when possible in [this pr](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/70)

## Testing
At the moment, our test coverage is *76%* class coverage and *38%* line. The disparity between these two is explained by certain difficult to test features of our code, namely our CLI and printing to console. To circumvent this, we have tested the related helper methods within these classes in order to verify they are working correctly.

There are not very many components that are difficult to test based on our design. Our general testing workflow is to check correctness, accuracy and at the very least display. In this context, correctness is whether the code does what it is meant to do and is generally defined by an ```assert``` statement. Accuracy is the performance of our code in niche cases, and making sure that we aren't just testing basic, surface cases. For certain classes, like `ASCIIFormatter` or `CommandLineIntergace` as neither of these really hold because you cannot test how "nice" something looks. Instead we simply make sure that the final string being printed to console is correct, as well as making sure that the helper methods within the class are also correct. 

We also use a MockAPI to test API related features of our code. This way, even if the API is not responding, unrelated tests will not fail which will make it far easier to troubleshoot as well as debug.

## Design Pattern Summary

### Strategy
This is design pattern is best exemplified by the "Filter" interface and it's subsequent implementations. The classes that implement it are:
- [InPersonFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/InPersonFilter.java)
- [SpaceFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/SpaceFilter.java)
- [CourseExclusionFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/CourseExclusionFilter.java)
- [ConflictFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/ConflictFilter.java)
- [TimeFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/TimeFilter.java)
- [ExcludeTimeFilter](https://github.com/CSC207-UofT/course-project-tut0201-007/blob/main/src/main/java/filters/ExcludeTimeFilter.java)

The Strategy Design Pattern is a collection of encapsulated algorithms, that can be slotted in and out with one another. This lets the user use whichever strategy they would like. In order to do so the core abstraction is implemented by some interface, and classes that use this carry the specific implementations. The "core abstraction" is our `Filter` interface, that uses the method `checkSchedule` which is implemented differently in all classes that implement `Filter`. Then, the user can use the UI outlined by `CommandLineInterface` to select which ones they would like to apply to their schedules.

### Facade
A Facade is a design pattern that this program uses to simplify the complexity of the system for certain classes. For example, a Facade is used in the `generateCourse()` method of `CourseCreator`. While the actual course generation required the use of the U of T API, the `Timeslot` and `Section` classes, and the `APIWorker` use-case, the methods simply requires a course id (i.e. CSC207) and a session (i.e F for fall semester). This design pattern reduces the required knowledge `CourseCreator` needs to perform its function.

### Template Method
The Template Method is a design pattern that we be used to improve the schedule export feature of the program. Currently the program is equipped to serialize schedule data in an .ics/.csv file format. Because the process of exporting a schedule has similar steps for these file types, the `Exporter` abstract class was created to act as a Template for the algorithm. The concrete subclasses `ICSExporter` and `CSVExporter` are designed to override some parts of the algorithm while retaining the main structure of the algorithm. This is also an application of the Open-closed principle.

The Template method was introduced in [this pull request](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/57), and was implemented in [this PR](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/62) and [this PR](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/69).

## Progress Report

### Open questions
- How do we improve the worst case runtime of our filters?
- What other factors impact course making decision and how can we make filters to address these factors?
- Are there any other ways we can make our UI more intuitve to use? Make it more accessable to people who are less familiar with the command line?
- Do we want to consider expanding the functionality to include browsing courses for selection?

### What has worked well so far
- Linking Github Issues with Projects has a great automated feature where cue cards are automatically linked with PR's where issues are cited, and automatically get moved to the column they should be in.
- Pull Request reviews have been an efficient and concise way to communicate each group member's thoughts on design decisions, code formatting, and any other miscellaneous questions about the commits.
- Our choice of entities, once we switched to using Sections to represent lecture or tutorial sections, made implementing schedule generation and ICS export/import much easier
- The choice to use the Strategy design pattern for Filter allowed us to develop a wide range of Filters, without much effort for integrating them with our general program.
- Use of the Template design pattern for exporters and importers of Schedules has made it frictionless to implement and integrate alternate options for exporting/importing

## Accessibility Report
### Question 1
* Principle 1: Our program does not currently adhere to principle 1, mostly because we only have a CLI. In the future, we can allow user customization of the CLI shortcuts, so users can select the keyboard inputs that are easiest for them. We can also allow the user to customize colors used in the CLI, to accomodate color blindness. Beyond this, we could add a GUI to allow alternate input like am mouse or joystick.
* Principle 2 (Flexibility in Use):
We allow the user to create a schedule through many methods such as through importing a pre existing file or you can choose to generate a schedule from scratch either all at once or through one by one generation. There are also different options for desired output format, such as ICS, CSV or JPG so that a variety of user needs can be met.
* Principle 3 (Simple and Intuitive Use):
We adhere to principle 3 by consistently using red for any negative responses during the scheduling creating prompt sequence. We also ensure that our CLI responses, especially for cases where there are errors, are as simple and easy to understand as possible.
* Principle 4 (Perceptive Information): In order to be accessible to all users, our program makes use of Principle 4 (Perceptive Information) of the Universal Design Principles. To display schedules, we use ASCII characters to formulate a pictoral representation of the schedule which is easier for the user to understand. When using the CLI, our instructions for program usage are clear and unambigious to maximize the user experience. We also bold and color code key words within the CLI to maximize legibility of essential information.
* Principle 5 (Tolerance for Error):
We check user input to our CLI, to verify that it is both the correct type of input, and also a reasonable input (for example, bounding possible inputs for number of courses to schedule to be positive). We also provide warnings whenever the user enters invalid input, as well as a repeated description of what type of input should be entered.
* Principle 6 (Low Physical Effort): We fulfill Principle 6 by providing shortcuts for Users in our CLI. For example, rather than needing to type out "Yes", or moving their moues and clicking a button, users only need to press "1" on their keyboard. This means users don't need to exert much force or physical effort. Also they can stay in their typing position, which is generally a neutral body position.
* Principle 7 (Size and Space for Approach and Use): We do not fulfill principle 7. However, this principle does not apply to this program, because it doesn't present any UI elements except on the computer screen, and the only required component is the keyboard. Therefore, physical space requirements for the program can't be affected by us.

### Q2

We would market our program towards U of T students, because it is a tool for scheduling courses at U of T. Specifically, since our program works through a CLI, which is a positive for users who prefer the command line over a GUI, we'd market towards U of T students who prefer a CLI experience. However, we have gone through great lengths to make our CLI as inviting and accessible as possible, so that people without CLI experience can still use the application. Since our program is automatically up to date, as it uses data directly from the U of T timestable, we can market this program towards students now, as well as in the future.

### Q3

Our program is less likely to be used by users who prefer the use of an input device that doesn't support directly inputting text into the CLI. Also, it is less likely to be used by non-students, because it is a course scheduling app, and less likely to be used by non-UofT students, because it can only schedule courses for UofT. It is also less likely to be used by non-technical people, since they may not be comfortable interacting with an application directly through the CLI.

### Group member contributions & plans

#### Rory
* Worked On:
  * design document
  * testing
  * CSV data serialization functionality
* Signifigant contributions: CSVExporter [(PR)](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/62) and CSVImporter [(PR)](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/73)
   * These PRs were a significant contribution to the project because it improved our support for data serialization and introduced design patterns that would make extending this functionality a much easier task.

#### Kenneth
* Worked On since Phase 2:
  * Design Document
  * Export Schedule to an image
  * Fix bug regarding year long courses that change location
  * Changed CLI File import dialogue to show all importable files
  * Accessibility report
 * Significant [PR](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/21):
  * This PR is a significant contribution because it allows our application to serialize schedules and export schedules to calendar applications. It also laid the groundwork for later exporters and importers.

#### Siddarth
* Phase 1 Major Contribution ([PR](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/6)):
  * Retriving JSON data from the Timetable API through `APIWorker`
  * Foundation of the app, all our data comes from here
* Phase 2 Major Contribution ([PR](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/61)):
  * ASCII Schedule rendering to console
  * Accessibility -> Makes it far more convenient for the easier
  * User is easily able to visualize the schedule, directly in the CLI
  * Additional functionality for the application
* Other contributions:
  * Filter + Strategy implementation in `SpaceFilter`
  * User Input
  * Optimization
  * Test coverage

#### Lorena
* Worked On:
  * Redesigning CLI output to be more user-friendly 
  * Implementing the course information display feature 
  * Accounting for course corequsites 
  * Bug fixes 
  * Design Document
* Significant Contribution 1: ([PR](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/85))
  * This PR is significant because it adds additional functionality to the application and enhances the user experience by giving them an option to view course information before creating a schedule with them.    
* Significant Contribution 2: ([PR](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/76))
  * This PR is significant because it redesigns the CLI output so that it is more accessible and intuitive for the user. The use of text colouring and formatting highlights instructions more effectively and minimizes ambiguity. 

#### Anton
* Phase 1 -Worked On:
  * Creating Controller class, decoupling from CommandLineInterface
  * Scheduler with permutation implementation using filters and serialization
  * User input and output
  * Design Document/Specification
* Phase 2 - Worked On:
[(PR)](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/63)
[(PR)](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/75)
  * Creating ExecutionState class, improving communication between Controller/CLI barrier
  * Creating ExcludeTimeFilter class, writing test cases. This was significant because it was easy: our design allowed us to extend the way Schedules are generated with no changes to Scheduler, and minimal additions to the CLI class. This demonstrated the importance of SOLID principles.
  * Implementing one-by-one course generation. This improves functionality of the program since the user can choose course sections while Schedules are being generated. This minimizes the user having to scroll through all possibiliies at the end of generation.

#### Baker
 * Phase 1 Major Contribution [PR](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/23):
  * Refactoring code base to properly store Section information
  * Implementing the Timeslot class and its functions such as conflictWith
  * Fixing code in the CourseCreator to be more readable and to use the new Section / Timeslot implementations
* Phase 2 Major Contribution [PR](https://github.com/CSC207-UofT/course-project-tut0201-007/pull/71):
  * Adding RateMyProfessor functionality web scraping / qraphql querying
  * Implemented ProfessorRatingFilter that ensures all courses in a schedule are of a certain quality
  * Prioritize sections taught by highly-graded profs
  * User is easily able to query this info directly in the CLI
  * Fixed bug with the conflictWith function in Timeslot
* Other contributions:
  * Building the original CLI during phase 0
  * Implementing Factory design pattern in the CourseCreator class
  * Improved Test coverage of workers, including JSON files to test the RateMyProfessor worker

