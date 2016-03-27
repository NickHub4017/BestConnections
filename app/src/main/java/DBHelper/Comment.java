package DBHelper;

/**
 * Created by Pasan Eramusugoda on 4/5/2015.
 */
public class Comment {
    private int id;
    private String unique_id;
    private String section_id;
    private String comment;
    private String datetime;

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }
}
