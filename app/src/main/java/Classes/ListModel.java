package Classes;

/**
 * Created by Pasan Eramusugoda on 4/24/2015.
 */
public class ListModel {
    private String Quote;
    private String PersonSource;
    private String Date;
    private String Question1;
    private String Question2;
    private String ImagePath;

    public String getDate() {
        return Date;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getPersonSource() {
        return PersonSource;
    }

    public String getQuestion1() {
        return Question1;
    }

    public String getQuestion2() {
        return Question2;
    }

    public String getQuote() {
        return Quote;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setPersonSource(String personSource) {
        PersonSource = personSource;
    }

    public void setQuestion1(String question1) {
        Question1 = question1;
    }

    public void setQuestion2(String question2) {
        Question2 = question2;
    }

    public void setQuote(String quote) {
        Quote = quote;
    }
}
