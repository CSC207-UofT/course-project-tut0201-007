package workers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import util.RateMyProfessorException;

public class ProfessorRatingScraper {
    private static Map<String, Double> previousScores = new HashMap<String, Double>();

    public static Double getProfessorRating(String firstName, String lastName)
            throws RateMyProfessorException {
        String fullName = firstName + " " + lastName;
        if (previousScores.containsKey(fullName)) {
            return previousScores.get(fullName);
        }
        String json = "";
        try {
            String data =
                    "{\"query\":\"query TeacherSearchResultsPageQuery(\\n"
                        + "  $query: TeacherSearchQuery!\\n"
                        + "  $schoolID: ID\\n"
                        + ") {\\n"
                        + "  search: newSearch {\\n"
                        + "    ...TeacherSearchPagination_search_1ZLmLD\\n"
                        + "  }\\n"
                        + "  school: node(id: $schoolID) {\\n"
                        + "    __typename\\n"
                        + "    ... on School {\\n"
                        + "      name\\n"
                        + "    }\\n"
                        + "    id\\n"
                        + "  }\\n"
                        + "}\\n"
                        + "\\n"
                        + "fragment TeacherSearchPagination_search_1ZLmLD on newSearch {\\n"
                        + "  teachers(query: $query, first: 8, after: \\\"\\\") {\\n"
                        + "    didFallback\\n"
                        + "    edges {\\n"
                        + "      cursor\\n"
                        + "      node {\\n"
                        + "        ...TeacherCard_teacher\\n"
                        + "        id\\n"
                        + "        __typename\\n"
                        + "      }\\n"
                        + "    }\\n"
                        + "    pageInfo {\\n"
                        + "      hasNextPage\\n"
                        + "      endCursor\\n"
                        + "    }\\n"
                        + "    resultCount\\n"
                        + "    filters {\\n"
                        + "      field\\n"
                        + "      options {\\n"
                        + "        value\\n"
                        + "        id\\n"
                        + "      }\\n"
                        + "    }\\n"
                        + "  }\\n"
                        + "}\\n"
                        + "\\n"
                        + "fragment TeacherCard_teacher on Teacher {\\n"
                        + "  id\\n"
                        + "  legacyId\\n"
                        + "  avgRating\\n"
                        + "  numRatings\\n"
                        + "  ...CardFeedback_teacher\\n"
                        + "  ...CardSchool_teacher\\n"
                        + "  ...CardName_teacher\\n"
                        + "  ...TeacherBookmark_teacher\\n"
                        + "}\\n"
                        + "\\n"
                        + "fragment CardFeedback_teacher on Teacher {\\n"
                        + "  wouldTakeAgainPercent\\n"
                        + "  avgDifficulty\\n"
                        + "}\\n"
                        + "\\n"
                        + "fragment CardSchool_teacher on Teacher {\\n"
                        + "  department\\n"
                        + "  school {\\n"
                        + "    name\\n"
                        + "    id\\n"
                        + "  }\\n"
                        + "}\\n"
                        + "\\n"
                        + "fragment CardName_teacher on Teacher {\\n"
                        + "  firstName\\n"
                        + "  lastName\\n"
                        + "}\\n"
                        + "\\n"
                        + "fragment TeacherBookmark_teacher on Teacher {\\n"
                        + "  id\\n"
                        + "  isSaved\\n"
                        + "}\\n"
                        + "\",\"variables\":{\"query\":{\"text\":\"TEACHER_NAME\",\"schoolID\":\"U2Nob29sLTE0ODQ=\",\"fallback\":true,\"departmentID\":null},\"schoolID\":\"U2Nob29sLTE0ODQ=\"}}";
            data = data.replace("TEACHER_NAME", fullName);
            HttpPost post = new HttpPost("https://www.ratemyprofessors.com/graphql");

            post.setHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:95.0) Gecko/20100101"
                            + " Firefox/95.0");
            post.setHeader("Accept", "*/*");
            post.setHeader("Accept-Language", "en-CA,en-US;q=0.7,en;q=0.3");
            post.setHeader("Accept-Encoding", "gzip, deflate, br");
            post.setHeader(
                    "Referer",
                    "https://www.ratemyprofessors.com/search/teachers?query=c%20baker&sid=U2Nob29sLTE0ODQ=");
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Basic dGVzdDp0ZXN0");
            post.setHeader("Origin", "https://www.ratemyprofessors.com");
            post.setHeader("Connection", "keep-alive");
            post.setHeader("Cookie", "ccpa-notice-viewed-02=true");
            post.setHeader("Sec-Fetch-Dest", "empty");
            post.setHeader("Sec-Fetch-Mode", "cors");
            post.setHeader("Sec-Fetch-Site", "same-origin");

            post.setEntity(new StringEntity(data));

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                    CloseableHttpResponse response = httpClient.execute(post)) {
                json = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            return 2.5;
        }
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        double rating = getRatingFromHTML(jsonObject);
        previousScores.put(fullName, rating);
        return rating;
    }

    public static double getRatingFromHTML(JsonObject json) throws RateMyProfessorException {
        String foundTeacher =
                json.getAsJsonObject("data")
                        .getAsJsonObject("search")
                        .getAsJsonObject("teachers")
                        .get("didFallback")
                        .toString();
        if (foundTeacher.equals("true")) {
            throw new RateMyProfessorException("Professor not found");
        }
        return json.getAsJsonObject("data")
                .getAsJsonObject("search")
                .getAsJsonObject("teachers")
                .getAsJsonArray("edges")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("node")
                .get("avgRating")
                .getAsDouble();
    }

    public static void main(String[] args) {
        try {
            System.out.println(getProfessorRating("a", "zaman"));
        } catch (Exception e) {
            System.out.println("sadge");
        }
    }
}
