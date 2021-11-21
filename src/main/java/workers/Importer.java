package workers;

import entities.Schedule;

import java.io.Reader;

/** An abstract class that provides a template for importing schedules with different file types (i.e. ICS, CSV, etc.) */
public abstract class Importer {

    public abstract Schedule importSchedule(Reader reader);

}
