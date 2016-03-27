package DBHelper;

/**
 * Created by Pasan Eramusugoda on 4/5/2015.
 */
public class Quiz {
    private int id;
    private String section_id;
    private String quiz;
    private int value;

    public String getSection_id() {
        return section_id;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
