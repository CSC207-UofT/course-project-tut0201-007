package workers;

import com.google.gson.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an API Worker. The class is meant to be used to interact with the API. A
 * worker is created for each class, and stores all API data.
 */
public class APIWorker {
    public JsonObject info;
    public List<String> semester;

    /**
     * Constructor that assigns ArrayLists of lecture and tutorial sessions.
     *
     * @param newId is the course id passed in, i.e. CSC207
     */
    public APIWorker(String newId) throws IOException {
        this.info = readUrl(newId).getAsJsonObject();
        this.semester = new ArrayList<>(this.info.keySet());
    }

    /**
     * Function that reads a valid url and turns it into a java JsonElement.
     *
     * @return The JsonElement obtained from the API url
     */
    private static JsonElement readUrl(String courseId) throws IOException {
        String api_template;
        if (!courseId.contains("TST")) {
            api_template =
                    "https://timetable.iit.artsci.utoronto.ca/api/20219/courses?org=&code=COURSENAME&section=&studyyear=&daytime=&weekday=&prof=&breadth=&deliverymode=&online=&waitlist=&available=&fyfcourse=&title=";
            try (java.io.InputStream is =
                    new java.net.URL(api_template.replace("COURSENAME", courseId)).openStream()) {
                String contents = new String(is.readAllBytes());
                // If the course is not present in the API, an empty list will be returned as the
                // contents
                if (contents.equals("[]")) {
                    throw new IOException("This course does not exist.");
                }
                return JsonParser.parseString(contents);
            }
        } else {
            File f = new File("src/test/mockapi/tst" + courseId.substring(3) + ".json");
            try (java.io.InputStream is = new java.io.FileInputStream(f)) {
                String contents = new String(is.readAllBytes());
                return JsonParser.parseString(contents);
            }
        }
    }

    @Override
    public String toString() {
        return this.semester.toString();
    }
}
