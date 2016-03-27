package DBHelper;

/**
 * Created by Pasan Eramusugoda on 4/5/2015.
 */
public class MyJournal {
    private int id;
    private String identity;
    private int type; //0=option mode, 1 = question mode
    private String question_text;
    private String question_text2;
    private String quote_text;
    private String person_source;
    private String date;
    private int chosen_option; //0=not selected, 1=like, 2=unlike
    private String image_path;

    public int getId() {
        return id;
    }

    public String getIdentity() {
        return identity;
    }

    public int getType() {
        return type;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public String getQuestion_text2() {
        return question_text2;
    }

    public String getQuote_text() {
        return quote_text;
    }

    public String getPerson_source() {
        return person_source;
    }

    public String getDate() {
        return date;
    }

    public int getChosen_option() {
        return chosen_option;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public void setQuestion_text2(String question_text2) {
        this.question_text2 = question_text2;
    }

    public void setQuote_text(String quote_text) {
        this.quote_text = quote_text;
    }

    public void setPerson_source(String person_source) {
        this.person_source = person_source;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setChosen_option(int chosen_option) {
        this.chosen_option = chosen_option;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
