package DBHelper;

/**
 * Created by Pasan Eramusugoda on 4/5/2015.
 */
public class Section {
    private int id;
    private String section_des;
    private String section_id;
    private String last_quote;
    private boolean section_finished;
    private int left_view;

    public int getLeft_view() {
        return left_view;
    }

    public boolean getSection_finished() {
        return section_finished;
    }

    public String getLast_quote() {
        return last_quote;
    }

    public int getId() {
        return id;
    }

    public String getSection_des() {
        return section_des;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setLeft_view(int left_view) {
        this.left_view = left_view;
    }

    public void setSection_finished(boolean section_finished) {
        this.section_finished = section_finished;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSection_des(String section_des) {
        this.section_des = section_des;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public void setLast_quote(String last_quote) {
        this.last_quote = last_quote;
    }
}
