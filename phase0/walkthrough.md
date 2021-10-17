The user inputs that they want to take CSC207 and CSC236. This is input through the CLI, which is implemented in our controller class. The controller parses the requested courses, and then uses our two use cases, CourseCreator and Scheduler. It uses CourseCreator to a specific course. Scheduler takes multiple course codes, and instantiates a Schedule. These both use APIWorker to get course info.

Then, we output the string representation of one of the Courses and the string representation of the Schedule through our controller class.


