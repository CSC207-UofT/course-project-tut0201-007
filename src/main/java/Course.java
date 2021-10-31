import java.lang.reflect.Array;
import java.util.*;

/** This class represents a course, which consists of lecture and tutorial sessions */
public class Course {

    private final String courseId;
    private final Map<String, ArrayList<Session>> lectures;
    private final Map<String, ArrayList<Session>> tutorials;

    private static final String LEC = "LEC";
    private static final String TUT = "TUT";

    /**
     * Constructs a course with an id and list of lecture and tutorial sessions
     *
     * @param courseId given id
     */
    public Course(String courseId) {
        this.courseId = courseId;
        this.lectures = new HashMap<String, ArrayList<Session>>();
        this.tutorials = new HashMap<String, ArrayList<Session>>();
    }

    /**
     * Adds sessions to their respective sections in the hashmap.
     *
     * All sessions must have type and section.
     *
     * For example, a session that is a lecture has its type identified with Session.
     */
    public void addSessions(ArrayList<Session> sessions) {
        String section;
        String type;
        Map<String, ArrayList<Session>> values;

        for(Session s : sessions) {
            type = s.getType();
            section = s.getSection();

            if (type.equals(LEC)) {
                values = this.lectures;
            } else if (type.equals(TUT)) {
                values = this.tutorials;
            } else {
                continue;
            }

            if(values.containsKey(section)) {
                values.get(section).add(s);
            } else {
                ArrayList<Session> temp = new ArrayList<>();
                temp.add(s);
                values.put(section, temp);
            }

            return;
        }
    }


    public String getCourseId() {
        return this.courseId;
    }

    public String[] getLectureSections() {
        return (String[]) this.lectures.keySet().toArray();
    }

    public String[] getTutorialSections() {
        return (String[]) this.tutorials.keySet().toArray();
    }


    /** Returns a clone of the ArrayList of all lecture sessions in a given section.
     * The getTutorials() method is very similar
     *
     * @param section the section code
     * @return all lecture sessions in this section
     */
    public List<Session> getLectures(String section) {
        if(lectures.containsKey(section)) {
            return (ArrayList<Session>) lectures.get(section).clone();
        } else {
            return new ArrayList<Session>();
        }
    }

    /** Returns the lecture sessions associated with the first key. If there is no first key,
     * the map 'lectures' must be empty, and it returns an empty ArrayList.
     *
     * @return lecture sessions with the first section key in hashmap
     */
    public List<Session> getLectures() {
        if (this.lectures.isEmpty()) {
            return new ArrayList<Session>();
        } else {
            String first_key = (String) lectures.keySet().toArray()[0];
            return (ArrayList<Session>) lectures.get(first_key).clone();
        }
    }

    public List<Session> getTutorials(String section) {
        if(tutorials.containsKey(section)) {
            return (ArrayList<Session>) tutorials.get(section).clone();
        } else {
            return new ArrayList<Session>();
        }
    }

    /** Returns the tutorial sessions associated with the first key. If there is no first key,
     * the map 'tutorials' must be empty, and it returns an empty ArrayList.
     *
     * @return tutorial sessions with the first section key in map
     */
    public List<Session> getTutorials() {
        if (this.tutorials.isEmpty()) {
            return new ArrayList<Session>();
        } else {
            String first_key = (String) tutorials.keySet().toArray()[0];
            return (ArrayList<Session>) tutorials.get(first_key).clone();
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(courseId + "\nLectures:\n");
        String tab = "    ";
        for (String key: lectures.keySet()) {
            for (Session lec: lectures.get(key)) {
                ret.append(tab + lec.toString() + "\n");
            }
        }

        ret.append("Tutorials:\n");
        for (String key: tutorials.keySet()) {
            for (Session lec: tutorials.get(key)) {
                ret.append(tab + lec.toString() + "\n");
            }
        }
        return ret.toString();
    }
}
