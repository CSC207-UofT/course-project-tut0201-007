package entities;

public class ASCIIFormatter {

    private final Schedule schedule;

    /**
     * Constructor that takes schedule to be turned into ASCII output.
     *
     * @param sched is the schedule to be formatted
     */
    public ASCIIFormatter(Schedule sched) {
        this.schedule = sched;
    }
}
