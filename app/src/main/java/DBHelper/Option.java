package DBHelper;

/**
 * Created by Pasan Eramusugoda on 4/5/2015.
 */
public class Option {
    private int id;
    private String unique_id;
    private boolean is_like;
    private boolean is_bullseye;

    public int getId() {
        return id;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public boolean getIs_like() {
        return is_like;
    }

    public boolean getIs_bullseye() {
        return is_bullseye;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public void setIs_bullseye(boolean is_bullseye) {
        this.is_bullseye = is_bullseye;
    }

    public void setIs_like(boolean is_like) {
        this.is_like = is_like;
    }
}