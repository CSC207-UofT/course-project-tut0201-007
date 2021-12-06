package workers;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import util.RateMyProfessorException;

import java.util.HashMap;
import java.util.Map;

public class ProfessorRatingScraper {
    private static Map<String, Double> previousScores = new HashMap<String, Double>();

    public static Double getProfessorRating(String firstName, String lastName) throws RateMyProfessorException {
        if (previousScores.containsKey(firstName + lastName)) {
            return previousScores.get(firstName + lastName);
        }
        String html;
        try {
            html = Jsoup.connect(
                    "https://www.ratemyprofessors.com/search/teachers?query="
                            + firstName
                            + " "
                            + lastName
                            + "&sid=U2Nob29sLTE0ODQ=")
                    .get()
                    .html();
        } catch (IOException e) {
            throw new RateMyProfessorException("RateMyProfessors webpage cannot be reached");
        }
        double rating = getRatingFromHTML(html);
        return rating;
    }

    public static double getRatingFromHTML(String html) throws RateMyProfessorException {
        Document doc = Jsoup.parse(html);
        Element teacher = doc.getElementsByClass("TeacherCard__StyledTeacherCard-syjs0d-0 dLJIlx").first();
        Double ret = -1.0;
        Boolean correctSchool = false;
        if (teacher == null) {
            throw new RateMyProfessorException("Professor not found");
        }
        for (Element e : teacher.getElementsByTag("div")) {
            if (e.className().contains("CardNumRatingNumber")) {
                ret = Double.parseDouble(e.text());
            }
            if (e.className().contains("CardSchool__School")) {
                if (e.text().equals("University of Toronto - St. George Campus")) {
                    correctSchool = true;
                }
            }
            if (correctSchool && ret != -1.0) {
                return ret;
            }
        }
        throw new RateMyProfessorException("Professor not found");
    }
}
