import com.google.gson.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class represents an API Worker. The class is meant to be used to interact with the API. A
 * worker is created for each class, and stores all API data.
 */
public class APIWorker {
    JsonObject info;
    ArrayList<String> semester;

    /**
     * Constructor that assigns ArrayLists of lecture and tutorial sessions.
     *
     * @param newId is the course id passed in, i.e. CSC207
     */
    public APIWorker(String newId, boolean mock) throws IOException {
        if (!mock) {
            this.info = readUrl(newId, false).getAsJsonObject();
        } else {
            this.info = readUrl(newId, true).getAsJsonArray().get(0).getAsJsonObject();
        }
        this.semester = new ArrayList<>(this.info.keySet());
    }

    /**
     * Function that reads a valid url and turns it into a java JsonElement.
     *
     * @return The JsonElement obtained from the API url
     */
    private static JsonElement readUrl(String courseId, boolean mock) throws IOException {
        String api_template;
        if (!mock) {
            api_template =
                    "https://timetable.iit.artsci.utoronto.ca/api/20219/courses?org=&code=COURSENAME&section=&studyyear=&daytime=&weekday=&prof=&breadth=&deliverymode=&online=&waitlist=&available=&fyfcourse=&title=";
        } else {
            api_template = "https://618bfe3bded7fb0017bb935e.mockapi.io/COURSENAME";
        }
        try (java.io.InputStream is =
                     new java.net.URL(api_template.replace("COURSENAME", courseId)).openStream()) {
            String contents = new String(is.readAllBytes());
            return JsonParser.parseString(contents);
        }
    }

    @Override
    public String toString() {
        return this.semester.toString();
    }
}
