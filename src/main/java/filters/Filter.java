package filters;

import entities.Schedule;

/**
 * An interface that will be responsible for checking schedules
 * based on given specifications.
 */
public interface Filter {
    public boolean checkSchedule(Schedule s);
}