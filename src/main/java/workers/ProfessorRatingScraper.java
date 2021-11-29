package workers;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

class ProfessorRatingScraper {
    public static double getRating(String firstName, String lastName) {
        String html;
        try {
            html =
                    Jsoup.connect(
                                    "https://www.ratemyprofessors.com/search/teachers?query="
                                            + firstName
                                            + " "
                                            + lastName
                                            + "&sid=U2Nob29sLTE0ODQ=")
                            .get()
                            .html();
        } catch (IOException e) {
            return 2.5;
        }
        Document doc = Jsoup.parse(html);
        Element teacher =
                doc.getElementsByClass("TeacherCard__StyledTeacherCard-syjs0d-0 dLJIlx").first();
        Double ret = -1.0;
        Boolean correctSchool = false;
        if (teacher == null) {
            return 2.5;
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
        return 2.5;
    }
}
