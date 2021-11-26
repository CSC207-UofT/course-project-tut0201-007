package workers;

import entities.Schedule;

import java.io.Reader;

/** An abstract class that provides a template for importing schedules with different file types (i.e. ICS, CSV, etc.) */
public interface Importer {

    /**
     * A method requiring that any import class has a method to import a Schedule object from a file
     *
     * @param reader The Reader object that parses from the file
     * @return A Schedule object that contains all the Sections from the file
     */
    Schedule importSchedule(Reader reader);

}
