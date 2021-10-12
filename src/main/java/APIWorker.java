import com.google.gson.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class APIWorker {
    private final String api_template =
            "https://timetable.iit.artsci.utoronto.ca/api/20219/courses?org=&code=COURSENAME&section=&studyyear=&daytime=&weekday=&prof=&breadth=&deliverymode=&online=&waitlist=&available=&fyfcourse=&title=";
    String course_JSON;

    public APIWorker(String new_id) {
        this.course_JSON = api_template.replace("COURSENAME", new_id);
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null) reader.close();
        }
    }

    public void main(String[] args) throws Exception {
        String json = readUrl(this.course_JSON);
        Gson payload = new Gson();
        HashMap<String, Object> map = new HashMap<>();
        map = (HashMap<String, Object>) payload.fromJson(json, map.getClass());

        System.out.println(map);
    }

    @Override
    public String toString() {
        return String.format(this.course_JSON);
    }
}
