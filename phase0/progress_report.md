# Specification Summary

TODO after updated specification
# CRC Model Summary
* Controller:
  * CommandLine, maps user input to calling commands
* Entities:
  * Schedule: Stores Lectures & Tutorials in schedule, and can add either Lecture or Tutorial
  * Course: Stores Lectures, Sessions, courseID for a course
  * Session: Represents Lecture or Tutorial with location and Date & Time
* Frameworks & Drivers:
  * APIWorker: Makes API request to U of T API, parses returned JSON payload and stores
* Use Cases:
  * Filter: Interface for a filter, like "No courses after 4 PM"
  * TimeFilter: Filters for courses only being within a certain time range
  * CourseCreator: Populates new Course Object using APIWorker
  * Scheduler: Generates Schedules using filters
  * IcsCreator: Create ICS file for a Session
# Scenario Walk-Through Summary
The program takes User Input of 2 courses, parses the courses with **CommandLine**, and creates a Schedule with Scheduler, and outputs the schedule to the CLI. It also uses CourseCreator to create a Course for one of the courses, and outputs that course to the CLI.

# Open Questions
* Is there a way we can use our Filters to make the schedule generation more efficient? Right now we plan to get all permutations, and check if they meet our Filter's specifications after

# What's Worked Well so Far
# Group Member Work Overview

**TODO**: Put "a brief summary of what each group member has been working on and what they plan to work on next.". Make sure formatting matches between different people.

Also, like you can BS the "To Work on" as long as it sounds reasonable. I doubt they care if work gets shifted around later
## Kenneth
* Worked On:
  * CRC Cards
  * Walkthrough
  * Refactoring
  * Progress Report
* To Work on:
  * Create UseCase for converting Session (ICSCreator)
  * Implement TimeFilter

