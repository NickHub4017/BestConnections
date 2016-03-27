package DBHelper;

/**
 * Created by Pasan Eramusugoda on 4/5/2015.
 */
public class Quote {
    private int id;
    private String section_id;
    private String unique_id;
    private int quote_id;
    private String quote_type;
    private String quote;
    private String person_source;
    private String date;
    private String image;
    private boolean answered;
    private boolean is_active;
    private int version;

    public int getVersion() {
        return version;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public int getId() {
        return id;
    }

    public String getQuote() {
        return quote;
    }

    public int getQuote_id() {
        return quote_id;
    }

    public boolean getAnswered() {
        return answered;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getPerson_source() {
        return person_source;
    }

    public String getQuote_type() {
        return quote_type;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public void setPerson_source(String person_source) {
        this.person_source = person_source;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setQuote_id(int quote_id) {
        this.quote_id = quote_id;
    }

    public void setQuote_type(String quote_type) {
        this.quote_type = quote_type;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
