import com.google.gson.*;
import java.io.IOException;

public class APIWorker {
    private final String id;
    String course_JSON;
    JsonObject info;

    public APIWorker(String new_id) throws IOException {
        this.id = new_id;
        this.info = readUrl().getAsJsonObject();
    }

    private JsonElement readUrl() throws IOException {
        String api_template =
                "https://timetable.iit.artsci.utoronto.ca/api/20219/courses?org=&code=COURSENAME&section=&studyyear=&daytime=&weekday=&prof=&breadth=&deliverymode=&online=&waitlist=&available=&fyfcourse=&title=";

        try(java.io.InputStream is = new java.net.URL(api_template.replace("COURSENAME", this.id)).openStream()) {
            String contents = new String(is.readAllBytes());
            return JsonParser.parseString(contents);
        }
    }

    @Override
    public String toString() {
        return this.course_JSON;
    }
}
