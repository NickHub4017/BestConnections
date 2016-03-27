package DBHelper;

/**
 * Created by Pasan Eramusugoda on 4/5/2015.
 */
public class Question {
    private int id;
    private String section_id;
    private String unique_id;
    private String question;
    private String answer;

    public String getUnique_id() {
        return unique_id;
    }

    public int getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }
}
